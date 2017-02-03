package io.github.kobakei.grenade;

import android.content.Context;
import android.content.Intent;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.github.kobakei.grenade.annotation.Extra;
import io.github.kobakei.grenade.annotation.Navigator;
import io.github.kobakei.grenade.annotation.OnActivityResult;
import io.github.kobakei.grenade.annotation.Optional;

@AutoService(Processor.class)
public class GrenadeProcessor extends AbstractProcessor {

    private static final boolean LOGGABLE = false;

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Types types;

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

    private static final Map<String, String> PUT_EXTRA_STATEMENTS_2 = new HashMap<String, String>() {{
        put("java.lang.Integer",        "intent.putExtra($S, $L)");
        put("java.lang.Long",           "intent.putExtra($S, $L)");
        put("java.lang.Short",          "intent.putExtra($S, $L)");
        put("java.lang.Float",          "intent.putExtra($S, $L)");
        put("java.lang.Double",         "intent.putExtra($S, $L)");
        put("java.lang.Boolean",        "intent.putExtra($S, $L)");
        put("java.lang.Byte",           "intent.putExtra($S, $L)");
        put("java.lang.Character",      "intent.putExtra($S, $L)");
        put("java.lang.String",         "intent.putExtra($S, $L)");
        put("java.lang.CharSequence",   "intent.putExtra($S, $L)");
        put("java.io.Serializable",     "intent.putExtra($S, $L)");
        put("android.os.Parcelable",    "intent.putExtra($S, $L)");
        put("android.os.Bundle",        "intent.putExtra($S, $L)");

        put("int[]",         "intent.putExtra($S, $L)");
        put("long[]",        "intent.putExtra($S, $L)");
        put("short[]",       "intent.putExtra($S, $L)");
        put("float[]",       "intent.putExtra($S, $L)");
        put("double[]",      "intent.putExtra($S, $L)");
        put("boolean[]",     "intent.putExtra($S, $L)");
        put("char[]",        "intent.putExtra($S, $L)");
        put("byte[]",        "intent.putExtra($S, $L)");
        put("java.lang.String[]",        "intent.putExtra($S, $L)");
        put("java.lang.CharSequence[]",        "intent.putExtra($S, $L)");
        put("android.os.Parcelable[]",        "intent.putExtra($S, $L)");

        put("java.util.ArrayList<java.lang.Integer>",       "intent.putIntegerArrayListExtra($S, $L)");
        put("java.util.ArrayList<java.lang.String>",        "intent.putStringArrayListExtra($S, $L)");
        put("java.util.ArrayList<java.lang.CharSequence>",  "intent.putCharSequenceArrayListExtra($S, $L)");
        put("java.util.ArrayList<android.os.Parcelable>",   "intent.putParcelableArrayListExtra($S, $L)");
    }};

    private static final Map<String, String> GET_EXTRA_STATEMENTS = new HashMap<String, String>() {{
        put("java.lang.Integer",        "intent.getIntExtra($S, 0)");
        put("java.lang.Long",           "intent.getLongExtra($S, 0L)");
        put("java.lang.Short",          "intent.getShortExtra($S, (short)0)");
        put("java.lang.Float",          "intent.getFloatExtra($S, 0.0f)");
        put("java.lang.Double",         "intent.getDoubleExtra($S, 0.0)");
        put("java.lang.Boolean",        "intent.getBooleanExtra($S, false)");
        put("java.lang.Byte",           "intent.getByteExtra($S, (byte)0)");
        put("java.lang.Character",      "intent.getCharExtra($S, (char)0)");
        put("java.lang.String",         "intent.getStringExtra($S)");
        put("java.lang.CharSequence",   "intent.getCharSequenceExtra($S)");
        put("java.io.Serializable",     "intent.getSerializableExtra($S)");
        put("android.os.Parcelable",    "intent.getParcelableExtra($S)");
        put("android.os.Bundle",        "intent.getBundleExtra($S)");

        put("int[]",                    "intent.getIntArrayExtra($S)");
        put("long[]",                   "intent.getLongArrayExtra($S)");
        put("short[]",                  "intent.getShortArrayExtra($S)");
        put("float[]",                  "intent.getFloatArrayExtra($S)");
        put("double[]",                 "intent.getDoubleArrayExtra($S)");
        put("boolean[]",                "intent.getBooleanArrayExtra($S)");
        put("char[]",                   "intent.getCharArrayExtra($S)");
        put("byte[]",                   "intent.getByteArrayExtra($S)");
        put("java.lang.String[]",       "intent.getStringArrayExtra($S)");
        put("java.lang.CharSequence[]", "intent.getCharSequenceArrayExtra($S)");
        put("android.os.Parcelable[]",  "intent.getParcelableArrayExtra($S)");

        put("java.util.ArrayList<java.lang.Integer>",       "intent.getIntegerArrayListExtra($S)");
        put("java.util.ArrayList<java.lang.String>",        "intent.getStringArrayListExtra($S)");
        put("java.util.ArrayList<java.lang.CharSequence>",  "intent.getCharSequenceArrayListExtra($S)");
        put("java.util.ArrayList<android.os.Parcelable>",   "intent.getParcelableArrayListExtra($S)");
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
        this.types = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Navigator.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        Class<Navigator> navigatorClass = Navigator.class;
        for (Element element : roundEnv.getElementsAnnotatedWith(navigatorClass)) {
            log("Found navigator");
            try {
                generateNavigator(element);
            } catch (IOException e) {
                logError("IO error");
            }
        }

        return true;
    }

    /**
     * Genaret FooActivityNavigator class
     * @param element Element annotated with @Navigator.
     * @throws IOException
     */
    private void generateNavigator(Element element) throws IOException {
        String className = element.getSimpleName().toString();
        String packageName = elements.getPackageOf(element).getQualifiedName().toString();
        String navigatorName = className + "Navigator";
        ClassName targetClass = ClassName.get(packageName, className);
        ClassName navigatorClass = ClassName.get(packageName, navigatorName);

        // Class
        TypeSpec.Builder navigatorBuilder = TypeSpec.classBuilder(navigatorName)
                .addJavadoc("Launcher of $T", targetClass)
                .addModifiers(Modifier.PUBLIC);

        // Launcher annotation
        Navigator navigator = element.getAnnotation(Navigator.class);
        String[] rules = navigator.value();

        // Find @Extra and @OnActivityResult
        List<Element> requiredExtraElements = new ArrayList<>();
        List<Element> optionalExtraElements = new ArrayList<>();
        List<Element> onActivityResultElements = new ArrayList<>();
        for (Element elem : element.getEnclosedElements()) {
            // Extra
            Extra extra = elem.getAnnotation(Extra.class);
            if (extra != null) {
                Optional optional = elem.getAnnotation(Optional.class);
                if (optional != null) {
                    log("Optional");
                    optionalExtraElements.add(elem);
                } else {
                    log("Required");
                    requiredExtraElements.add(elem);
                }
            }

            // OAR
            OnActivityResult onActivityResult = elem.getAnnotation(OnActivityResult.class);
            if (onActivityResult != null) {
                onActivityResultElements.add(elem);
            }
        }

        // fields
        log("Adding fields");
        for (Element e : requiredExtraElements) {
            addField(navigatorBuilder, e);
        }
        for (Element e : optionalExtraElements) {
            addField(navigatorBuilder, e);
        }

        // flag field
        FieldSpec flagFieldSpec = FieldSpec.builder(TypeName.INT, "flags", Modifier.PRIVATE)
                .build();
        navigatorBuilder.addField(flagFieldSpec);

        // action field
        FieldSpec actionFieldSpec = FieldSpec.builder(TypeName.get(String.class), "action", Modifier.PRIVATE)
                .build();
        navigatorBuilder.addField(actionFieldSpec);

        // Constructor
        log("Adding constructors");
        if (rules.length == 0) {
            addConstructor(navigatorBuilder, requiredExtraElements);
        } else {
            for (String rule : rules) {
                addConstructor(navigatorBuilder, requiredExtraElements, rule);
            }
        }

        // set option value method
        log("Add optional methods");
        for (Element optionalExtraElement : optionalExtraElements) {
            addOptionalExtraMethod(navigatorBuilder, navigatorClass, optionalExtraElement);
        }

        // add flags method
        log("Add flags method");
        addFlagsMethod(navigatorBuilder, navigatorClass);

        // set action method
        log("Add action method");
        addActionMethod(navigatorBuilder, navigatorClass);

        // build method
        log("Add build method");
        addBuildMethod(navigatorBuilder, targetClass, requiredExtraElements, optionalExtraElements);

        // (static) inject method
        log("Add inject method");
        addInjectMethod(navigatorBuilder, targetClass, requiredExtraElements, optionalExtraElements);

        // (static) resultFor method for each @OnActivityResult
        log("Add resultFor method");
        addResultForMethods(navigatorBuilder, onActivityResultElements);

        // (static) onActivityResult method
        log("Add onActivity method");
        if (onActivityResultElements.size() > 0) {
            addOnActivityResultMethod(navigatorBuilder, targetClass, onActivityResultElements);
        }

        // Write
        JavaFile.builder(packageName, navigatorBuilder.build())
                .build()
                .writeTo(filer);
    }

    /**
     * Add field
     * @param navigatorBuilder
     * @param extraElement
     */
    private void addField(TypeSpec.Builder navigatorBuilder, Element extraElement) {
        String fieldName = extraElement.getSimpleName().toString();
        TypeName fieldType = TypeName.get(extraElement.asType());
        FieldSpec fieldSpec = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE)
                .build();
        navigatorBuilder.addField(fieldSpec);
    }

    /**
     * Add constructor with params
     * @param navigatorBuilder
     * @param requiredElements
     */
    private void addConstructor(TypeSpec.Builder navigatorBuilder, List<Element> requiredElements) {
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addJavadoc("Constructor with required params")
                .addModifiers(Modifier.PUBLIC);
        for (Element e : requiredElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            constructorSpecBuilder.addParameter(fieldType, fieldName)
                    .addStatement("this.$L = $L", fieldName, fieldName);
        }
        navigatorBuilder.addMethod(constructorSpecBuilder.build());
    }

    /**
     * Add constructor with params and rule
     * @param navigatorBuilder
     * @param requiredExtraElements
     * @param rule
     */
    private void addConstructor(TypeSpec.Builder navigatorBuilder, List<Element> requiredExtraElements, String rule) {
        List<String> tokens = Arrays.asList(rule.split(","));
        MethodSpec.Builder constructorSpecBuilder = MethodSpec.constructorBuilder()
                .addJavadoc("Constructor with required params")
                .addModifiers(Modifier.PUBLIC);
        for (Element e : requiredExtraElements) {
            String fieldName = e.getSimpleName().toString();
            TypeName fieldType = TypeName.get(e.asType());
            if (tokens.contains(fieldName)) {
                constructorSpecBuilder.addParameter(fieldType, fieldName)
                        .addStatement("this.$L = $L", fieldName, fieldName);
            }
        }
        navigatorBuilder.addMethod(constructorSpecBuilder.build());
    }

    /**
     * Add optional value method.
     * @param navigatorBuilder
     * @param navigatorClass
     * @param optionalExtraElement
     */
    private void addOptionalExtraMethod(TypeSpec.Builder navigatorBuilder, ClassName navigatorClass,
                                        Element optionalExtraElement) {
        String fieldName = optionalExtraElement.getSimpleName().toString();
        TypeName fieldType = TypeName.get(optionalExtraElement.asType());
        MethodSpec setOptionalSpec = MethodSpec.methodBuilder(fieldName)
                .addJavadoc("Set optional field")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fieldType, fieldName)
                .returns(navigatorClass)
                .addStatement("this.$L = $L", fieldName, fieldName)
                .addStatement("return this")
                .build();
        navigatorBuilder.addMethod(setOptionalSpec);
    }

    /**
     * Add flags method
     * @param navigatorBuilder
     * @param navigatorClass
     */
    private void addFlagsMethod(TypeSpec.Builder navigatorBuilder, ClassName navigatorClass) {
        MethodSpec flagsMethod = MethodSpec.methodBuilder("flags")
                .addJavadoc("Add intent flags")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.INT, "flags")
                .returns(navigatorClass)
                .addStatement("this.flags = flags")
                .addStatement("return this")
                .build();
        navigatorBuilder.addMethod(flagsMethod);
    }

    /**
     * Add action method
     * @param navigatorBuilder
     * @param navigatorClass
     */
    private void addActionMethod(TypeSpec.Builder navigatorBuilder, ClassName navigatorClass) {
        MethodSpec actionMethod = MethodSpec.methodBuilder("action")
                .addJavadoc("Set action")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(String.class), "action")
                .returns(navigatorClass)
                .addStatement("this.action = action")
                .addStatement("return this")
                .build();
        navigatorBuilder.addMethod(actionMethod);
    }

    /**
     * Add static inject method
     * @param navigatorBuilder
     * @param targetClass
     * @param requiredExtraElements
     * @param optionalExtraElements
     */
    private void addInjectMethod(TypeSpec.Builder navigatorBuilder,
                                 ClassName targetClass,
                                 List<Element> requiredExtraElements,
                                 List<Element> optionalExtraElements) {
        MethodSpec.Builder injectSpecBuilder = MethodSpec.methodBuilder("inject")
                .addJavadoc("Inject fields of activity from intent")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(targetClass, "target")
                .addParameter(INTENT_CLASS, "intent");
        for (Element e : requiredExtraElements) {
            addGetExtraStatement(injectSpecBuilder, e, false);
        }
        for (Element e : optionalExtraElements) {
            addGetExtraStatement(injectSpecBuilder, e, true);
        }
        navigatorBuilder.addMethod(injectSpecBuilder.build());
    }

    /**
     * Add build method
     * @param navigatorBuilder
     * @param targetClass
     * @param requiredExtraElements
     * @param optionalExtraElements
     */
    private void addBuildMethod(TypeSpec.Builder navigatorBuilder,
                                ClassName targetClass,
                                List<Element> requiredExtraElements,
                                List<Element> optionalExtraElements) {
        MethodSpec.Builder buildSpecBuilder = MethodSpec.methodBuilder("build")
                .addJavadoc("Build intent")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(CONTEXT_CLASS, "context")
                .returns(INTENT_CLASS)
                .addStatement("$T intent = new $T(context, $T.class)", INTENT_CLASS, INTENT_CLASS, targetClass);
        for (Element e : requiredExtraElements) {
            addPutExtraStatement(buildSpecBuilder, e);
        }
        for (Element e : optionalExtraElements) {
            addPutExtraStatement(buildSpecBuilder, e);
        }
        buildSpecBuilder
                .addStatement("intent.addFlags(this.flags)")
                .addStatement("intent.setAction(this.action)")
                .addStatement("return intent")
                .build();
        navigatorBuilder.addMethod(buildSpecBuilder.build());
    }

    /**
     * Add resultForXXX method
     * @param navigatorBuilder
     * @param onActivityResultElements
     */
    private void addResultForMethods(TypeSpec.Builder navigatorBuilder,
                                     List<Element> onActivityResultElements) {
        for (Element oarElement : onActivityResultElements) {

            ExecutableType executableType = (ExecutableType) oarElement.asType();
            if (executableType.getParameterTypes().size() <= 0) {
                continue;
            }

            String methodName = oarElement.getSimpleName().toString();

            MethodSpec.Builder resultForSpecBuilder = MethodSpec.methodBuilder("resultFor" + StringUtils.beginCap(methodName))
                    .addJavadoc("Create result intent")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(INTENT_CLASS);
            resultForSpecBuilder
                    .addStatement("$T intent = new $T()", INTENT_CLASS, INTENT_CLASS);

            for (int i = 0; i < executableType.getParameterTypes().size(); i++) {
                TypeMirror paramTypeMirror = executableType.getParameterTypes().get(i);
                String key = "param" + i;

                TypeName paramType = TypeName.get(paramTypeMirror);
                TypeName boxedParamType = paramType.box();

                resultForSpecBuilder.addParameter(paramType, key);
                resultForSpecBuilder.addStatement(PUT_EXTRA_STATEMENTS_2.get(boxedParamType.toString()), key, key);
            }

            resultForSpecBuilder
                    .addStatement("return intent");
            navigatorBuilder.addMethod(resultForSpecBuilder.build());
        }
    }

    /**
     * Add onActivityResult method
     * @param navigatorBuilder
     * @param targetClass
     * @param onActivityResultElements
     */
    private void addOnActivityResultMethod(TypeSpec.Builder navigatorBuilder,
                                           ClassName targetClass,
                                           List<Element> onActivityResultElements) {
        MethodSpec.Builder onActivityResultSpecBuilder = MethodSpec.methodBuilder("onActivityResult")
                .addJavadoc("Call this method in your Activity's onActivityResult")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(targetClass, "target")
                .addParameter(TypeName.INT, "requestCode")
                .addParameter(TypeName.INT, "resultCode")
                .addParameter(INTENT_CLASS, "intent");
        for (Element e : onActivityResultElements) {
            String methodName = e.getSimpleName().toString();
            OnActivityResult oar = e.getAnnotation(OnActivityResult.class);

            onActivityResultSpecBuilder.beginControlFlow(
                    "if (requestCode == $L && java.util.Arrays.asList($L).contains(resultCode))",
                    oar.requestCode(),
                    StringUtils.join(oar.resultCodes()));

            ExecutableType executableType = (ExecutableType) e.asType();
            String args = "";
            for (int i = 0; i < executableType.getParameterTypes().size(); i++) {
                TypeMirror paramTypeMirror = executableType.getParameterTypes().get(i);
                String key = "param" + i;

                TypeName paramType = TypeName.get(paramTypeMirror).box();

                String statement = "$T $L = " + GET_EXTRA_STATEMENTS.get(paramType.toString());

                onActivityResultSpecBuilder
                        .addStatement(statement, paramTypeMirror, key, key);

                args += key;
                if (i < executableType.getParameterTypes().size() - 1) {
                    args += ",";
                }
            }

            onActivityResultSpecBuilder
                    .addStatement("target.$L($L)", methodName, args)
                    .endControlFlow();
        }
        navigatorBuilder.addMethod(onActivityResultSpecBuilder.build());
    }

    /**
     * Add putXXXExtra statement to build method
     * @param buildSpecBuilder
     * @param e
     */
    private void addPutExtraStatement(MethodSpec.Builder buildSpecBuilder, Element e) {
        String fieldName = e.getSimpleName().toString();
        Extra extra = e.getAnnotation(Extra.class);
        String keyName = extra.key().length() > 0 ? extra.key() : fieldName;
        TypeName fieldType = TypeName.get(e.asType()).box();
        if (shouldUseParceler(e)) {
            buildSpecBuilder.addStatement(PARCELER_PUT_EXTRA_STATEMENT, keyName, PARCELER_CLASS, fieldName);
            return;
        } else {
            String statement = PUT_EXTRA_STATEMENTS.get(fieldType.toString());
            if (statement != null) {
                buildSpecBuilder.addStatement(statement, keyName, fieldName);
                return;
            }
        }
        logError("[putExtra] Unsupported type: " + fieldType.toString());
    }

    /**
     * Add getXXXExtra statement to inject method
     * @param injectSpecBuilder
     * @param e
     * @param isOptional
     */
    private void addGetExtraStatement(MethodSpec.Builder injectSpecBuilder, Element e, boolean isOptional) {
        String fieldName = e.getSimpleName().toString();
        Extra extra = e.getAnnotation(Extra.class);
        String keyName = extra.key().length() > 0 ? extra.key() : fieldName;
        TypeName fieldType = TypeName.get(e.asType()).box();
        if (isOptional) {
            injectSpecBuilder.beginControlFlow("if (intent.hasExtra($S))", fieldName);
        }
        if (shouldUseParceler(e)) {
            injectSpecBuilder.addStatement(PARCELER_GET_EXTRA_STATEMENT, fieldName, PARCELER_CLASS, keyName);
        } else {
            String statement = GET_EXTRA_STATEMENTS.get(fieldType.toString());
            if (statement != null) {
                statement = "target.$L = " + statement;
                injectSpecBuilder.addStatement(statement, fieldName, keyName);
            } else {
                logError("[getExtra] Unsupported type: " + fieldType.toString());
            }
        }
        if (isOptional) {
            injectSpecBuilder.endControlFlow();
        }
    }

    private boolean hasAnnotation(Element e, String name) {
        if (e == null) {
            return false;
        }
        for (AnnotationMirror annotation : e.getAnnotationMirrors()) {
            if (annotation.getAnnotationType().asElement().getSimpleName().toString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldUseParceler(Element fieldElement) {
        log("field = " + fieldElement.getSimpleName().toString());
        TypeElement typeElement = (TypeElement) types.asElement(fieldElement.asType());
        if (typeElement == null) {
            return false;
        }
        if (hasAnnotation(typeElement, "Parcel")) {
            return true;
        }
        DeclaredType declaredType = (DeclaredType) fieldElement.asType();
        for (TypeMirror genericParam : declaredType.getTypeArguments()) {
            log("gp = " + genericParam.toString());
            if (hasAnnotation(types.asElement(genericParam), "Parcel")) {
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
