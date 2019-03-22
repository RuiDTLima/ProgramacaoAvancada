package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.function.Predicate;

public class ProfilerTranslator implements Translator {
    /**
     * This is the code template that replaces a field write. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_WRITER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addWriter(\"%s\");";

    /**
     * This is the code template that replaces a field read. It maintains the original code with the instruction
     * proceed.
     */
    private static final String INCR_READER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addReader(\"%s\");";

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
                    replaceFieldAccess(fa, fieldAccess -> fieldAccess.isStatic() || (fieldAccess.getClassName().equals(className) && fieldAccess.isWriter()));
                }
            });
        }

        for (CtMethod ctMethod : ctClass.getDeclaredMethods())
            ctMethod.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    replaceFieldAccess(fa, FieldAccess::isStatic);
                }
            });
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
    private static void replaceFieldAccess(FieldAccess fa, Predicate<FieldAccess> fieldAccessPredicate) throws CannotCompileException {
        if (fieldAccessPredicate.test(fa))
            return;
        if (fa.isWriter())
            fa.replace(String.format(INCR_WRITER_TEMPLATE, fa.getClassName()));
        else
            fa.replace(String.format(INCR_READER_TEMPLATE, fa.getClassName()));
    }
}
