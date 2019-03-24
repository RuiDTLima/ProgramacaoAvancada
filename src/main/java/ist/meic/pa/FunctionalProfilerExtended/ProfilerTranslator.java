package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import java.util.Arrays;

public class ProfilerTranslator implements Translator {
    /**
     * This is the code template that replaces a field writes. It maintains the original code with the instruction
     * proceed and checks if the field being accessed doesn't belong to a different object instance
     */
    private static final String INCR_WRITER_IN_CONSTRUCTOR_TEMPLATE = "$_ = $proceed($$); if(!this.equals($0)) ist.meic.pa.FunctionalProfilerExtended.Register.addWriter($0.getClass().getName(), \"%s\");";

    /**
     * This is the code template that replaces a field read. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_READER_IN_CONSTRUCTOR_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfilerExtended.Register.addReader($0.getClass().getName(), \"%s\");";

    /**
     * This is the code template that replaces a field write. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_WRITER_IN_METHOD_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfilerExtended.Register.addWriter($0.getClass().getName(), \"%s\");";

    /**
     * This is the code template that replaces a field read. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_READER_IN_METHOD_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfilerExtended.Register.addReader($0.getClass().getName(), \"%s\");";

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
            if (ctClass.isInterface() || ctClass.getAnnotation(IgnoreInstrumentation.class) != null)
                return;
        } catch (ClassNotFoundException e) {
            System.out.println("An error occurred while get annotations");
        }

        instrumentConstructor(ctClass);
        instrumentsMethod(ctClass);
    }

    /**
     * Analyse the constructors in <b>ctClass</b> to add the corresponding code template. A constructor will be ignored
     * if it contains the annotation <i>IgnoreInstrumentation</i>.
     * @see <a href="ist.meic.pa.FunctionalProfilerExtended.IgnoreInstrumentation">IgnoreInstrumentation</a>
     * @param ctClass represents the class being analysed.
     */
    private void instrumentConstructor(CtClass ctClass) {
        Arrays.stream(ctClass.getDeclaredConstructors())
                .filter(ctConstructor -> !hasIgnoreInstrumentationAnnotation(ctConstructor))
                .forEach(ctConstructor -> {
                    try {
                        ctConstructor.instrument(instrumentFieldInConstructor());
                    } catch (CannotCompileException e) {
                        System.out.println(String.format("Cannot instrument the constructor %S", ctConstructor.getName()));
                    }
                });
    }

    /**
     * Analyse the methods in <b>ctClass</b> to add the corresponding code template. A method will be ignored
     * if it contains the annotation <i>IgnoreInstrumentation</i>.
     * @see <a href="ist.meic.pa.FunctionalProfilerExtended.IgnoreInstrumentation">IgnoreInstrumentation</a>
     * @param ctClass represents the class being analysed.
     */
    private void instrumentsMethod(CtClass ctClass) {
        Arrays.stream(ctClass.getDeclaredMethods())
                .filter(ctMethod -> !hasIgnoreInstrumentationAnnotation(ctMethod))
                .forEach(ctMethod -> {
                    try {
                        ctMethod.instrument(instrumentFieldInMethod());
                    } catch (CannotCompileException e) {
                        System.out.println(String.format("Cannot instrument the method %S", ctMethod.getName()));
                    }
                });
    }

    private boolean hasIgnoreInstrumentationAnnotation(CtMember ctMember) {
        try {
            return ctMember.getAnnotation(IgnoreInstrumentation.class) != null;
        } catch (ClassNotFoundException e) {
            System.out.println("Could find the annotation class.");
            return false;
        }
    }

    private ExprEditor instrumentFieldInConstructor() {
        return new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                CtField ctField = getField(fa);
                if (ctField == null) return;
                replaceFieldAccessInConstructor(fa, ctField);
            }
        };
    }

    private ExprEditor instrumentFieldInMethod() {
        return new ExprEditor() {
            public void edit(FieldAccess fa) throws CannotCompileException {
                CtField ctField = getField(fa);
                if (ctField == null) return;
                replaceFieldAccessInMethod(fa, ctField);
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
     * It will check if fa is writer or reader and replace the code with INCR_WRITER_IN_CONSTRUCTOR_TEMPLATE or
     * INCR_READER_IN_CONSTRUCTOR_TEMPLATE respectively.
     *
     * @param fa FieldAccess that will be replaced
     * @throws CannotCompileException Exception rethrown from the method replace of fa
     */
    private void replaceFieldAccessInConstructor(FieldAccess fa, CtField ctField) throws CannotCompileException {
        if (hasIgnoreInstrumentationAnnotation(ctField) || fa.isStatic())
            return;
        if (fa.isWriter())
            fa.replace(String.format(INCR_WRITER_IN_CONSTRUCTOR_TEMPLATE, fa.getFieldName()));
        else
            fa.replace(String.format(INCR_READER_IN_CONSTRUCTOR_TEMPLATE, fa.getFieldName()));
    }

    /**
     * Check if predicate is true. If so nothing is done. Otherwise the method will replace the code where fa is at.
     * It will check if fa is writer or reader and replace the code with INCR_WRITER_IN_METHOD_TEMPLATE or INCR_READER_IN_METHOD_TEMPLATE
     * respectively.
     *
     * @param fa FieldAccess that will be replaced
     * @throws CannotCompileException Exception rethrown from the method replace of fa
     */
    private void replaceFieldAccessInMethod(FieldAccess fa, CtField ctField) throws CannotCompileException {
        if (hasIgnoreInstrumentationAnnotation(ctField) || fa.isStatic())
            return;
        if (fa.isWriter())
            fa.replace(String.format(INCR_WRITER_IN_METHOD_TEMPLATE, fa.getFieldName()));
        else
            fa.replace(String.format(INCR_READER_IN_METHOD_TEMPLATE, fa.getFieldName()));
    }
}
