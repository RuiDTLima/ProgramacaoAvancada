package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.io.IOException;

public class ProfilerTranslator implements Translator {
    private static final String INCR_WRITER_TEMPLATE = "writerCount = writerCount + 1;System.out.println(\"WriterCount: \" + writerCount);";
    private static final String INCR_READER_TEMPLATE = "readerCount = readerCount + 1;System.out.println(\"ReaderCount: \" + readerCount);";

    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {

        CtClass ctClass = pool.get(className);

        CtConstructor[] declaredConstructors = ctClass.getDeclaredConstructors();

        if (ctClass.isInterface())
            return;
        /*if (declaredConstructors.length == 0)
            return;
*/
        System.out.println(className);

        CtField readerField = CtField.make("private static int readerCount = 0;", ctClass);
        CtField writerField = CtField.make("private static int writerCount = 0;", ctClass);

        ctClass.addField(readerField);
        ctClass.addField(writerField);

        for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
            declaredMethod.instrument(new ExprEditor(){
                public void edit(FieldAccess fa) throws CannotCompileException {
                    if (fa.isStatic())
                        return;

                    int line = fa.getLineNumber();
                    if (fa.isWriter()) {
                        int newLine = declaredMethod.insertAt(fa.getLineNumber(), INCR_WRITER_TEMPLATE);
                        String ola = "ola";
                    } else {
                        int newLine = declaredMethod.insertAt(fa.getLineNumber(), INCR_READER_TEMPLATE);
                        int nLine = fa.getLineNumber();
                        String ola = "ola";
                    }
                }
            });
        }

        try {
            ctClass.writeFile("C:\\Users\\rui_l\\Ambiente de Trabalho");
        } catch (IOException e) {
            System.out.println("ola");
        }
    }
}
