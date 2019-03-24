package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ProfilerTranslator implements Translator {
    /**
     * This is the code template that checks if the instance that belongs to where this code is injected is not equals
     * to the instance that a field belongs. It maintains the original code with the instruction proceed.
     */
    private static final String INCR_WRITER_IN_CONSTRUCTOR_TEMPLATE = "$_ = $proceed($$); if(!this.equals($0)) ist.meic.pa.FunctionalProfiler.Register.addWriter($0.getClass().getName());";

    /**
     * This is the code template that replaces a field write. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_WRITER_IN_METHOD_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addWriter($0.getClass().getName());";

    /**
     * This is the code template that replaces a field read. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_READER_IN_METHOD_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addReader($0.getClass().getName());";

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

        if (ctClass.isInterface())
            return;

        for (CtConstructor ctConstructor : ctClass.getDeclaredConstructors()) {
            ctConstructor.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    replaceFieldAccessInConstructor(fa);
                }
            });
        }

        for (CtMethod ctMethod : ctClass.getDeclaredMethods())
            ctMethod.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    replaceFieldAccessInMethod(fa);
                }
            });

    }

    /**
     * This method is called when a FieldAccess fa is from a constructor.
     *
     * @param fa FieldAccess that will be replaced
     * @throws CannotCompileException Exception rethrown from the method replace of fa
     */
    private static void replaceFieldAccessInConstructor(FieldAccess fa) throws CannotCompileException {
        if (fa.isStatic())
            return;
        if (fa.isWriter())
            fa.replace(INCR_WRITER_IN_CONSTRUCTOR_TEMPLATE);
        else
            fa.replace(INCR_READER_IN_METHOD_TEMPLATE);
    }

    /**
     * This method is called when a FieldAccess fa is from a method.
     *
     * @param fa FieldAccess that will be replaced
     * @throws CannotCompileException Exception rethrown from the method replace of fa
     */
    private static void replaceFieldAccessInMethod(FieldAccess fa) throws CannotCompileException {
        if (fa.isStatic())
            return;
        if (fa.isWriter())
            fa.replace(INCR_WRITER_IN_METHOD_TEMPLATE);
        else
            fa.replace(INCR_READER_IN_METHOD_TEMPLATE);
    }
}
