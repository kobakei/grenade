package io.github.kobakei.grenade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Launcher;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "io.github.kobakei.grenade.annotation.Launcher",
        "io.github.kobakei.grenade.annotation.Extra"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class GrenadeProcessor extends AbstractProcessor {

    private static final boolean LOGGABLE = true;

    private Filer filer;
    private Messager messager;
    private Elements elements;

    private static final ClassName INTENT_CLASS = ClassName.get(Intent.class);
    private static final ClassName CONTEXT_CLASS = ClassName.get(Context.class);
    private static final ClassName STRING_CLASS = ClassName.get(String.class);
    private static final ClassName PARCELABLE_CLASS = ClassName.get(Parcelable.class);
    private static final ClassName BUNDLE_CLASS = ClassName.get(Bundle.class);
    private static final ClassName CHAR_SEQUENCE_CLASS = ClassName.get(CharSequence.class);

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elements = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Class<Launcher> launcherClass = Launcher.class;
        for (Element element : roundEnv.getElementsAnnotatedWith(launcherClass)) {
            log("Found launcher");
            try {
                generateBuilder(element);
            } catch (IOException e) {
                logError("IO error");
            }
        }

        return true;
    }

    private void generateBuilder(Element element) throws IOException {
        String className = element.getSimpleName().toString();
        String packageName = elements.getPackageOf(element).getQualifiedName().toString();
        String intentBuilderName = className + "IntentBuilder";
        ClassName targetClass = ClassName.get(packageName, className);

        // Class
        TypeSpec.Builder intentBuilderBuilder = TypeSpec.classBuilder(intentBuilderName)
                .addModifiers(Modifier.PUBLIC);

        // Launcher annotation
        Launcher launcher = element.getAnnotation(Launcher.class);
        String[] rules = launcher.value();

        // Extras
        List<Element> requiredElements = new ArrayList<>();
        List<Element> optionalElements = new ArrayList<>();
        for (Element elem : element.getEnclosedElements()) {
            Extra extra = elem.getAnnotation(Extra.class);
            if (extra != null) {
                if (hasAnnotation(elem, "Nullable")) {
                    log("Optional");
                    optionalElements.add(elem);
                } else {
                    log("Required");
                    requiredElements.add(elem);
                }
            }
        }

        // fields
        log("Adding fields");
        for (Element e : requiredElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            log("name is " + fieldName);
            log("type is " + fieldType.toString());
            FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
                    .build();
            intentBuilderBuilder.addField(fieldSpec);
        }
        for (Element e : optionalElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
                    .build();
            intentBuilderBuilder.addField(fieldSpec);
        }

        // flag field
        FieldSpec flagFieldSpec = FieldSpec.builder(TypeName.INT, "flags", Modifier.PRIVATE)
                .build();
        intentBuilderBuilder.addField(flagFieldSpec);

        // Constructor
        log("Adding constructors");
        if (rules.length == 0) {
            addConstructor(intentBuilderBuilder, requiredElements);
        } else {
            for (String rule : rules) {
                addConstructor(intentBuilderBuilder, requiredElements, rule);
            }
        }

        // set option value method
        log("Add optional methods");
        for (Element e : optionalElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            MethodSpec setOptionalSpec = MethodSpec.methodBuilder(fieldName)
                    .addJavadoc("Set optional field")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(fieldType, fieldName)
                    .returns(ClassName.get(packageName, intentBuilderName))
                    .addStatement("this.$L = $L", fieldName, fieldName)
                    .addStatement("return this")
                    .build();
            intentBuilderBuilder.addMethod(setOptionalSpec);
        }

        // add flags method
        log("Add flags method");
        MethodSpec flagsMethod = MethodSpec.methodBuilder("flags")
                .addJavadoc("Add intent flags")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.INT, "flags")
                .returns(ClassName.get(packageName, intentBuilderName))
                .addStatement("this.flags = flags")
                .addStatement("return this")
                .build();
        intentBuilderBuilder.addMethod(flagsMethod);

        // build method
        log("Add build method");
        MethodSpec.Builder buildSpecBuilder = MethodSpec.methodBuilder("build")
                .addJavadoc("Build intent")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT_CLASS, "context")
                .returns(INTENT_CLASS)
                .addStatement("$T intent = new $T(context, $T.class)", INTENT_CLASS, INTENT_CLASS, targetClass);
        for (Element e : requiredElements) {
            String fieldName = e.getSimpleName().toString();
            buildSpecBuilder.addStatement("intent.putExtra($S, this.$L)", fieldName, fieldName);
        }
        for (Element e : optionalElements) {
            String fieldName = e.getSimpleName().toString();
            buildSpecBuilder.addStatement("intent.putExtra($S, this.$L)", fieldName, fieldName);
        }
        buildSpecBuilder
                .addStatement("intent.addFlags(this.flags)")
                .addStatement("return intent")
                .build();
        intentBuilderBuilder.addMethod(buildSpecBuilder.build());

        // (static) inject method
        log("Add inject method");
        MethodSpec.Builder injectSpecBuilder = MethodSpec.methodBuilder("inject")
                .addJavadoc("Inject fields of activity from intent")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(targetClass, "target")
                .addParameter(INTENT_CLASS, "intent");
        for (Element e : requiredElements) {
            addGetExtraStatement(injectSpecBuilder, e);
        }
        for (Element e : optionalElements) {
            addGetExtraStatement(injectSpecBuilder, e);
        }
        intentBuilderBuilder.addMethod(injectSpecBuilder.build());

        // Write
        JavaFile.builder(packageName, intentBuilderBuilder.build())
                .build()
                .writeTo(filer);
    }

    /**
     * Add constructor with params
     * @param intentBuilderBuilder
     * @param requiredElements
     */
    private void addConstructor(TypeSpec.Builder intentBuilderBuilder, List<Element> requiredElements) {
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addJavadoc("Constructor with required params")
                .addModifiers(Modifier.PUBLIC);
        for (Element e : requiredElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            constructorSpecBuilder.addParameter(fieldType, fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName);
        }
        intentBuilderBuilder.addMethod(constructorSpecBuilder.build());
    }

    /**
     * Add constructor with params and rule
     * @param intentBuilderBuilder
     * @param requiredElements
     * @param rule
     */
    private void addConstructor(TypeSpec.Builder intentBuilderBuilder, List<Element> requiredElements, String rule) {
        List<String> tokens = Arrays.asList(rule.split(", "));
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addJavadoc("Constructor with required params")
                .addModifiers(Modifier.PUBLIC);
        for (Element e : requiredElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            if (tokens.contains(fieldName)) {
                constructorSpecBuilder.addParameter(fieldType, fieldName)
                        .addStatement("this.$L = $L", fieldName, fieldName);
            }
        }
        intentBuilderBuilder.addMethod(constructorSpecBuilder.build());
    }

    /**
     * Add getXXXExtra statemento to inject method
     * @param injectSpecBuilder
     * @param e
     */
    private void addGetExtraStatement(MethodSpec.Builder injectSpecBuilder, Element e) {
        String fieldName = e.getSimpleName().toString();
        TypeName fieldType = TypeName.get(e.asType());
        if (fieldType.equals(TypeName.INT)) {
            injectSpecBuilder.addStatement("target.$L = intent.getIntExtra($S, 0)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.LONG)) {
            injectSpecBuilder.addStatement("target.$L = intent.getLongExtra($S, 0L)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.SHORT)) {
            injectSpecBuilder.addStatement("target.$L = intent.getShortExtra($S, (short)0)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.FLOAT)) {
            injectSpecBuilder.addStatement("target.$L = intent.getFloatExtra($S, 0.0f)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.DOUBLE)) {
            injectSpecBuilder.addStatement("target.$L = intent.getDoubleExtra($S, 0.0)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.BOOLEAN)) {
            injectSpecBuilder.addStatement("target.$L = intent.getBooleanExtra($S, false)", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.CHAR)) {
            injectSpecBuilder.addStatement("target.$L = intent.getCharExtra($S, '0')", fieldName, fieldName);
        } else if (fieldType.equals(TypeName.BYTE)) {
            injectSpecBuilder.addStatement("target.$L = intent.getByteExtra($S, (byte)0)", fieldName, fieldName);
        } else if (fieldType.equals(STRING_CLASS)) {
            injectSpecBuilder.addStatement("target.$L = intent.getStringExtra($S)", fieldName, fieldName);
        } else if (fieldType.equals(PARCELABLE_CLASS)) {
            injectSpecBuilder.addStatement("target.$L = intent.getParcelableExtra($S)", fieldName, fieldName);
        } else if (fieldType.equals(BUNDLE_CLASS)) {
            injectSpecBuilder.addStatement("target.$L = intent.getBundleExtra($S)", fieldName, fieldName);
        } else if (fieldType.equals(CHAR_SEQUENCE_CLASS)) {
            injectSpecBuilder.addStatement("target.$L = intent.getCharSequenceExtra($S)", fieldName, fieldName);
        } else if (fieldType.equals(ArrayTypeName.of(TypeName.INT))) {
            injectSpecBuilder.addStatement("target.$L = intent.getIntArrayExtra($S)", fieldName, fieldName);
        } else {
            logError("Unsupported type");
        }
    }

    private boolean hasAnnotation(Element e, String name) {
        for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void log(String msg) {
        if (LOGGABLE) {
            this.messager.printMessage(Diagnostic.Kind.OTHER, msg);
        }
    }

    private void logError(String msg) {
        this.messager.printMessage(Diagnostic.Kind.ERROR, msg);
    }
}
