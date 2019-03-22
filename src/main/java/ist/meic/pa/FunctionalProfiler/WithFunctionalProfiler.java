package ist.meic.pa.FunctionalProfiler;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.Translator;

public class WithFunctionalProfiler {

    public static void main(String[] args) throws Throwable {
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.delegateLoadingOf("ist.meic.pa.FunctionalProfiler.Register");
        classLoader.addTranslator(pool, translator);
        classLoader.run(className, arguments);

        System.out.println("Total reads " + Register.getTotalReader() + " Total writes " + Register.getTotalWriter());
        Register.getCounters().forEach((s, readWriteCounter) -> System.out.println(s + " reads " + readWriteCounter.getReader() + " writes " + readWriteCounter.getWriter()));
    }
}