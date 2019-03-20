package ist.meic.pa.FunctionalProfiler;

import javassist.*;

public class WithFunctionalProfiler {
    public static void main(String[] args) throws Throwable {
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        CtClass ctClass = pool.get(className);

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.addTranslator(pool, translator);
        classLoader.run(className, arguments);

        ctClass.writeFile();
        ctClass.writeFile("C:\\Users\\rui_l\\Ambiente de Trabalho");
    }
}