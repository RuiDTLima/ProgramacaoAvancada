package ist.meic.pa.FunctionalProfiler;

import javassist.*;

public class WithFunctionalProfiler {
    public static void main(String[] args) {
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.delegateLoadingOf("ist.meic.pa.FunctionalProfiler.Register");

        try {
            classLoader.addTranslator(pool, translator);
        } catch (NotFoundException | CannotCompileException e) {
            System.out.println("An error occurred while adding the translator to the classLoader.");
            System.exit(1);
        }

        try {
            classLoader.run(className, arguments);
        } catch (ClassNotFoundException e) {
            System.out.println("The class provided could not be found.");
            System.exit(2);
        } catch (Throwable throwable) {
            System.out.println("An error occurred while executing the application.");
            System.exit(3);
        }

        System.out.println("Total reads: " + Register.getTotalReader() + " Total writes: " + Register.getTotalWriter());
        Register.getCounters().forEach((s, readWriteCounter) -> System.out.println(String.format("class %s -> reads: %d writes: %d", s, readWriteCounter.getReader(), readWriteCounter.getWriter())));
    }
}