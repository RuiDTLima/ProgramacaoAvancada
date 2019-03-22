package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.function.Predicate;

public class ProfilerTranslator implements Translator {
    private static final String INCR_WRITER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addWriter(\"%s\");";
    private static final String INCR_READER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addReader(\"%s\");";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

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

    private static void replaceFieldAccess(FieldAccess fa, Predicate<FieldAccess> fieldAccessPredicate) throws CannotCompileException {
        if (fieldAccessPredicate.test(fa))
            return;
        if (fa.isWriter())
            fa.replace(String.format(INCR_WRITER_TEMPLATE, fa.getClassName()));
        else
            fa.replace(String.format(INCR_READER_TEMPLATE, fa.getClassName()));
    }
}
