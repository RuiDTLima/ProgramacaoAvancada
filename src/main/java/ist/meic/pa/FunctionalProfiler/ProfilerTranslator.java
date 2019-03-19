package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.Arrays;

public class ProfilerTranslator implements Translator {
    public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
    }

    public void onLoad(ClassPool pool, String className) throws NotFoundException, CannotCompileException {
        CtClass ctClass = pool.get(className);

        CtConstructor[] declaredConstructors = ctClass.getDeclaredConstructors();

        if (declaredConstructors.length == 0)
            return;

        CtField[] fields = Arrays.stream(ctClass.getDeclaredFields())
                .filter(ctField -> !Modifier.isStatic(ctField.getModifiers()))
                .toArray(CtField[]::new);

        System.out.println(className);

        //ctClass.addField(new CtField());

        for (CtMethod declaredMethod : ctClass.getDeclaredMethods()) {
            declaredMethod.instrument(new ExprEditor(){
                public void edit(FieldAccess fa) throws CannotCompileException {

                    if (fa.isStatic())
                        return;

                    if (fa.isWriter()) {
                        String name = fa.getFieldName();
                        System.out.println("E escrito. " + name);
                    } else {
                        String name = fa.getFieldName();
                        System.out.println("E lido. " + name);
                    }
                }
            });
        }
    }
}
