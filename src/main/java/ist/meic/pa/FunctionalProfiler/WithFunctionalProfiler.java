package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import java.util.ArrayList;
import java.util.List;

public class WithFunctionalProfiler {
    private static CtClass mainClass;
    private static final String PRINT_COUNTERS_METHOD_TEMPLATE = "private static void printCounters() throws NoSuchFieldException, IllegalAccessException {\n" +
            "        for (int i = 0; i < objects.size(); i++) {\n" +
            "            String className = objects.get(i).getClass().getName();\n" +
            "            java.lang.reflect.Field readerCount = objects.get(i).getClass().getDeclaredField(\"readerCount\");\n" +
            "            readerCount.setAccessible(true);\n" +
            "\n" +
            "            java.lang.reflect.Field writerCount = objects.get(i).getClass().getDeclaredField(\"writerCount\");\n" +
            "            writerCount.setAccessible(true);\n" +
            "\n" +
            "            java.lang.System.out.println(className + \" -> reads: \" + readerCount.get(null) + \" writes: \" + writerCount.get(null));\n" +
            "        }" +
            "    }";

    private static List objects = new ArrayList();

    private static void printCounters() throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < objects.size(); i++) {
            String className = objects.get(i).getClass().getName();
            java.lang.reflect.Field readerCount = objects.get(i).getClass().getDeclaredField("readerCount");
            readerCount.setAccessible(true);

            java.lang.reflect.Field writerCount = objects.get(i).getClass().getDeclaredField("writerCount");
            writerCount.setAccessible(true);

            java.lang.System.out.println(className + " -> reads: " + readerCount.get(null) + " writes: " + writerCount.get(null));
        }
    }

    public static CtClass getMainClass() {
        return mainClass;
    }

    public static void main(String[] args) throws Throwable {
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        mainClass = pool.get(className);

        CtField readerField = CtField.make("private static java.util.List objects = new java.util.ArrayList();", mainClass);
        mainClass.addField(readerField);

        CtMethod method = CtMethod.make(PRINT_COUNTERS_METHOD_TEMPLATE, mainClass);
        mainClass.addMethod(method);

        mainClass.getDeclaredMethod("main").insertAfter("printCounters();");

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.addTranslator(pool, translator);
        classLoader.run(className, arguments);

        mainClass.writeFile();
        // TODO check use
    }
}