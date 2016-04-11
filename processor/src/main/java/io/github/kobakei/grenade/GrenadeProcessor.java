package io.github.kobakei.grenade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import io.github.kobakei.grenade.annotation.WithParceler;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "io.github.kobakei.grenade.annotation.Launcher",
        "io.github.kobakei.grenade.annotation.Extra",
        "org.parceler.Parcel"
})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class GrenadeProcessor extends AbstractProcessor {

    private static final boolean LOGGABLE = true;

    private Filer filer;
    private Messager messager;
    private Elements elements;

    private static final ClassName INTENT_CLASS = ClassName.get(Intent.class);
    private static final ClassName CONTEXT_CLASS = ClassName.get(Context.class);
    private static final ClassName PARCELER_CLASS = ClassName.get("org.parceler", "Parcels");

    private static final Map<String, String> PUT_EXTRA_STATEMENTS = new HashMap<String, String>() {{
        put("java.lang.Integer",        "intent.putExtra($S, this.$L)");
        put("java.lang.Long",           "intent.putExtra($S, this.$L)");
        put("java.lang.Short",          "intent.putExtra($S, this.$L)");
        put("java.lang.Float",          "intent.putExtra($S, this.$L)");
        put("java.lang.Double",         "intent.putExtra($S, this.$L)");
        put("java.lang.Boolean",        "intent.putExtra($S, this.$L)");
        put("java.lang.Byte",           "intent.putExtra($S, this.$L)");
        put("java.lang.Character",      "intent.putExtra($S, this.$L)");
        put("java.lang.String",         "intent.putExtra($S, this.$L)");
        put("java.lang.CharSequence",   "intent.putExtra($S, this.$L)");
        put("java.io.Serializable",     "intent.putExtra($S, this.$L)");
        put("android.os.Parcelable",    "intent.putExtra($S, this.$L)");
        put("android.os.Bundle",        "intent.putExtra($S, this.$L)");

        put("int[]",         "intent.putExtra($S, this.$L)");
        put("long[]",        "intent.putExtra($S, this.$L)");
        put("short[]",       "intent.putExtra($S, this.$L)");
        put("float[]",       "intent.putExtra($S, this.$L)");
        put("double[]",      "intent.putExtra($S, this.$L)");
        put("boolean[]",     "intent.putExtra($S, this.$L)");
        put("char[]",        "intent.putExtra($S, this.$L)");
        put("byte[]",        "intent.putExtra($S, this.$L)");
        put("java.lang.String[]",        "intent.putExtra($S, this.$L)");
        put("java.lang.CharSequence[]",        "intent.putExtra($S, this.$L)");
        put("android.os.Parcelable[]",        "intent.putExtra($S, this.$L)");

        put("java.util.ArrayList<java.lang.Integer>",       "intent.putIntegerArrayListExtra($S, this.$L)");
        put("java.util.ArrayList<java.lang.String>",        "intent.putStringArrayListExtra($S, this.$L)");
        put("java.util.ArrayList<java.lang.CharSequence>",  "intent.putCharSequenceArrayListExtra($S, this.$L)");
        put("java.util.ArrayList<android.os.Parcelable>",   "intent.putParcelableArrayListExtra($S, this.$L)");
    }};

    private static final Map<String, String> GET_EXTRA_STATEMENTS = new HashMap<String, String>() {{
        put("java.lang.Integer",        "target.$L = intent.getIntExtra($S, 0)");
        put("java.lang.Long",           "target.$L = intent.getLongExtra($S, 0L)");
        put("java.lang.Short",          "target.$L = intent.getShortExtra($S, (short)0)");
        put("java.lang.Float",          "target.$L = intent.getFloatExtra($S, 0.0f)");
        put("java.lang.Double",         "target.$L = intent.getDoubleExtra($S, 0.0)");
        put("java.lang.Boolean",        "target.$L = intent.getBooleanExtra($S, false)");
        put("java.lang.Byte",           "target.$L = intent.getByteExtra($S, (byte)0)");
        put("java.lang.Character",      "target.$L = intent.getCharExtra($S, (char)0)");
        put("java.lang.String",         "target.$L = intent.getStringExtra($S)");
        put("java.lang.CharSequence",   "target.$L = intent.getCharSequenceExtra($S)");
        put("java.io.Serializable",     "target.$L = intent.getSerializableExtra($S)");
        put("android.os.Parcelable",    "target.$L = intent.getParcelableExtra($S)");
        put("android.os.Bundle",        "target.$L = intent.getBundleExtra($S)");

        put("int[]",                    "target.$L = intent.getIntArrayExtra($S)");
        put("long[]",                   "target.$L = intent.getLongArrayExtra($S)");
        put("short[]",                  "target.$L = intent.getShortArrayExtra($S)");
        put("float[]",                  "target.$L = intent.getFloatArrayExtra($S)");
        put("double[]",                 "target.$L = intent.getDoubleArrayExtra($S)");
        put("boolean[]",                "target.$L = intent.getBooleanArrayExtra($S)");
        put("char[]",                   "target.$L = intent.getCharArrayExtra($S)");
        put("byte[]",                   "target.$L = intent.getByteArrayExtra($S)");
        put("java.lang.String[]",       "target.$L = intent.getStringArrayExtra($S)");
        put("java.lang.CharSequence[]", "target.$L = intent.getCharSequenceArrayExtra($S)");
        put("android.os.Parcelable[]",  "target.$L = intent.getParcelableArrayExtra($S)");

        put("java.util.ArrayList<java.lang.Integer>",       "target.$L = intent.getIntegerArrayListExtra($S)");
        put("java.util.ArrayList<java.lang.String>",        "target.$L = intent.getStringArrayListExtra($S)");
        put("java.util.ArrayList<java.lang.CharSequence>",  "target.$L = intent.getCharSequenceArrayListExtra($S)");
        put("java.util.ArrayList<android.os.Parcelable>",   "target.$L = intent.getParcelableArrayListExtra($S)");
    }};

    // Parceler
    private static final String PARCELER_PUT_EXTRA_STATEMENT = "intent.putExtra($S, $T.wrap(this.$L))";
    private static final String PARCELER_GET_EXTRA_STATEMENT = "target.$L = $T.unwrap(intent.getParcelableExtra($S))";


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
            addField(intentBuilderBuilder, e);
        }
        for (Element e : optionalElements) {
            addField(intentBuilderBuilder, e);
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
            addPutExtraStatement(buildSpecBuilder, e);
        }
        for (Element e : optionalElements) {
            addPutExtraStatement(buildSpecBuilder, e);
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
     * Add field
     * @param intentBuilderBuilder
     * @param e
     */
    private void addField(TypeSpec.Builder intentBuilderBuilder, Element e) {
        String fieldName = e.getSimpleName().toString();
        TypeName fieldType = TypeName.get(e.asType());
        FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
                .build();
        intentBuilderBuilder.addField(fieldSpec);
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
     * Add putXXXExtra statement to build method
     * @param buildSpecBuilder
     * @param e
     */
    private void addPutExtraStatement(MethodSpec.Builder buildSpecBuilder, Element e) {
        String fieldName = e.getSimpleName().toString();
        TypeName fieldType = TypeName.get(e.asType()).box();
        if (withParceler(e)) {
            buildSpecBuilder.addStatement(PARCELER_PUT_EXTRA_STATEMENT, fieldName, PARCELER_CLASS, fieldName);
            return;
        } else {
            String statement = PUT_EXTRA_STATEMENTS.get(fieldType.toString());
            if (statement != null) {
                buildSpecBuilder.addStatement(statement, fieldName, fieldName);
                return;
            }
        }
        logError("Unsupported type: " + fieldType.toString());
    }

    /**
     * Add getXXXExtra statement to inject method
     * @param injectSpecBuilder
     * @param e
     */
    private void addGetExtraStatement(MethodSpec.Builder injectSpecBuilder, Element e) {
        String fieldName = e.getSimpleName().toString();
        TypeName fieldType = TypeName.get(e.asType()).box();
        if (withParceler(e)) {
            injectSpecBuilder.addStatement(PARCELER_GET_EXTRA_STATEMENT, fieldName, PARCELER_CLASS, fieldName);
            return;
        } else {
            String statement = GET_EXTRA_STATEMENTS.get(fieldType.toString());
            if (statement != null) {
                injectSpecBuilder.addStatement(statement, fieldName, fieldName);
                return;
            }
        }
        logError("Unsupported type: " + fieldType.toString());
    }

    private boolean hasAnnotation(Element e, String name) {
        for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean withParceler(Element e) {
        WithParceler withParceler = e.getAnnotation(WithParceler.class);
        return withParceler != null;
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
