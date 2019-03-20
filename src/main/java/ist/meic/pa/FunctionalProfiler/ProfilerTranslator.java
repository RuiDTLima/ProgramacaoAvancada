package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import java.io.IOException;

public class ProfilerTranslator implements Translator {
    private static final String INCR_WRITER_TEMPLATE = "$_ = $proceed($$); writerCount = writerCount + 1;System.out.println(\"WriterCount: \" + writerCount);";
    private static final String INCR_READER_TEMPLATE = "$_ = $proceed($$); readerCount = readerCount + 1;System.out.println(\"ReaderCount: \" + readerCount);";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);

        if (ctClass.isInterface())
            return;

        CtClass mainClass = WithFunctionalProfiler.getMainClass();
        /*if (!ctClass.equals(mainClass)) {
            mainClass.getField().
        }*/

        System.out.println(className);

        CtField readerField = CtField.make("private static int readerCount = 0;", ctClass);
        CtField writerField = CtField.make("private static int writerCount = 0;", ctClass);

        ctClass.addField(readerField);
        ctClass.addField(writerField);

        for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
            declaredMethod.instrument(new ExprEditor() {
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.isStatic())
                        return;
                    if (fa.isWriter()) {
                        fa.replace(INCR_WRITER_TEMPLATE);
                    } else {
                        fa.replace(INCR_READER_TEMPLATE);
                    }
                }
            });
        }

        try {
            ctClass.writeFile();
        } catch (IOException e) {
            System.out.println("ola");
        }
    }
}
