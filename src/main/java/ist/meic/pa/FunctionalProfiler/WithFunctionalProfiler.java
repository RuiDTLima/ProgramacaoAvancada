package ist.meic.pa.FunctionalProfiler;

import javassist.*;
import java.util.ArrayList;
import java.util.List;

public class WithFunctionalProfiler {
    private static CtClass mainClass;
    private static final String PRINT_COUNTERS_METHOD_TEMPLATE = "private static void printCounters() {\n" +
            "        objects.forEach(o -> {\n" +
            "            String className = o.getClass().getName();\n" +
            "            try {\n" +
            "                Field readerCount = o.getClass().getDeclaredField(\"readerCount\");\n" +
            "                readerCount.setAccessible(true);\n" +
            "                \n" +
            "                Field writerCount = o.getClass().getDeclaredField(\"writerCount\");\n" +
            "                writerCount.setAccessible(true);\n" +
            "                \n" +
            "                System.out.println(String.format(\"%s -> reads: %d writes: %d\", className, readerCount.get(null), writerCount.get(null)));\n" +
            "            } catch (NoSuchFieldException | IllegalAccessException e) {\n" +
            "                e.printStackTrace();\n" +
            "            }\n" +
            "        });\n" +
            "    }";

    private static List<Object> objects = new ArrayList<>();

    public static CtClass getMainClass() {
        return mainClass;
    }

    public static void main(String[] args) throws Throwable {
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        mainClass = pool.get(className);

        CtField readerField = CtField.make("private static java.util.List<Object> objects = new java.util.ArrayList<>();", mainClass);
        mainClass.addField(readerField);

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.addTranslator(pool, translator);
        classLoader.run(className, arguments);

        CtMethod method = CtMethod.make(PRINT_COUNTERS_METHOD_TEMPLATE, mainClass);
        mainClass.addMethod(method);

        mainClass.getDeclaredMethod("main").insertAfter("printCounters()");

        mainClass.writeFile();
        // TODO check use
    }
}