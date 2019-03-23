package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.Arrays;
import java.util.function.BiPredicate;

public class ProfilerTranslator implements Translator {
    /**
     * This is the code template that replaces a field write. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_WRITER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfilerExtended.Register.addWriter($0.getClass().getName(), \"%s\");";

    /**
     * This is the code template that replaces a field read. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_READER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfilerExtended.Register.addReader($0.getClass().getName(), \"%s\");";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    /**
     * The onLoad method will be called for every class that will be loaded that isn't a interface. In the class'
     * declared constructors the static fields or the fields written to that belong to that class are ignored. In the
     * class' declared methods the static fields will be ignored.
     *
     * @param pool      See interface Translator
     * @param className See interface Translator
     * @throws NotFoundException      See interface Translator
     * @throws CannotCompileException See interface Translator
     */
    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);

        try {
            if (ctClass.isInterface() || ctClass.getAnnotation(IgnoreInstrumentation.class) != null)//.getAnnotations()..getAnnotation(IgnoreInstrumentation.class))
                return;
        } catch (ClassNotFoundException e) {
            System.out.println("An error occurred while get annotations");
        }

        instrumentConstructor(className, ctClass);

        instrumentsMethod(ctClass);
    }

    private void instrumentConstructor(String className, CtClass ctClass) {
        Arrays.stream(ctClass.getDeclaredConstructors())
                .filter(ctConstructor -> {
                    try {
                        return ctConstructor.getAnnotation(IgnoreInstrumentation.class) == null;
                    } catch (ClassNotFoundException e) {
                        System.out.println("Could find the annotation class.");
                        return false;
                    }
                })
                .forEach(ctConstructor -> {
                    try {
                        ctConstructor.instrument(instrumentField((fieldAccess, ctField) ->
                                ctField.hasAnnotation(IgnoreInstrumentation.class)
                                        || fieldAccess.isStatic()
                                        || (fieldAccess.getClassName().equals(className) && fieldAccess.isWriter())
                        ));
                    } catch (CannotCompileException e) {
                        System.out.println(String.format("Cannot instrument the constructor %S", ctConstructor.getName()));
                    }
                });
    }

    private void instrumentsMethod(CtClass ctClass) {
        Arrays.stream(ctClass.getDeclaredMethods())
                .filter(ctMethod -> {
                    try {
                        return ctMethod.getAnnotation(IgnoreInstrumentation.class) == null;
                    } catch (ClassNotFoundException e) {
                        System.out.println("Could find the annotation class.");
                        return false;
                    }
                })
                .forEach(ctMethod -> {
                    try {
                        ctMethod.instrument(instrumentField((fieldAccess, ctField) ->
                                ctField.hasAnnotation(IgnoreInstrumentation.class) || fieldAccess.isStatic()));
                    } catch (CannotCompileException e) {
                        System.out.println(String.format("Cannot instrument the method %S", ctMethod.getName()));
                    }
                });
    }

    private ExprEditor instrumentField(BiPredicate<FieldAccess, CtField> fieldAccessPredicate) {
        return new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                CtField ctField = getField(fa);
                if (ctField == null) return;
                replaceFieldAccess(fa, ctField, fieldAccessPredicate);
            }
        };
    }

    private CtField getField(FieldAccess fa) {
        final CtField ctField;
        try {
            ctField = fa.getField();
        } catch (NotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return ctField;
    }

    /**
     * Check if predicate is true. If so nothing is done. Otherwise the method will replace the code where fa is at.
     * It will check if fa is writer or reader and replace the code with INCR_WRITER_TEMPLATE or INCR_READER_TEMPLATE
     * respectively.
     *
     * @param fa                   FieldAccess that will be replaced
     * @param fieldAccessPredicate Predicate to remove unwanted FieldAccess fa
     * @throws CannotCompileException Exception rethrown from the method replace of fa
     */
    private static void replaceFieldAccess(FieldAccess fa, CtField ctField, BiPredicate<FieldAccess, CtField> fieldAccessPredicate) throws CannotCompileException {
        if (fieldAccessPredicate.test(fa, ctField))
            return;

        if (fa.isWriter())
            fa.replace(String.format(INCR_WRITER_TEMPLATE, fa.getFieldName()));
        else
            fa.replace(String.format(INCR_READER_TEMPLATE, fa.getFieldName()));
    }
}
