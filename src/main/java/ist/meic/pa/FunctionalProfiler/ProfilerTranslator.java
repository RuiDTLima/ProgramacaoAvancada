package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ProfilerTranslator implements Translator {
    private static final String INCR_WRITER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addWriter(\"%s\");";
    private static final String INCR_READER_TEMPLATE = "$_ = $proceed($$); ist.meic.pa.FunctionalProfiler.Register.addReader(\"%s\");";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);

        if (ctClass.isInterface())
            return;

        /*if (!ctClass.equals(mainClass)) {
            mainClass.getField().
        }*/

        System.out.println(className);

        for (CtMethod declaredMethod : ctClass.getDeclaredMethods())
            declaredMethod.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.isStatic())
                        return;
                    if (fa.isWriter())
                        fa.replace(String.format(INCR_WRITER_TEMPLATE, className));
                    else
                        fa.replace(String.format(INCR_READER_TEMPLATE, className));
                }
            });
    }
}
