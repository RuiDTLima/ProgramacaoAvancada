package ist.meic.pa.FunctionalProfilerExtended;

import javassist.*;

import java.util.Set;

public class WithFunctionalProfiler {
    private static final String TOTAL_READS_WRITES = "Total reads: %d Total writes: %d";
    private static final String CLASS_COUNTERS_OUTPUT = "class %s -> reads: %d writes: %d";
    private static final String FIELD_COUNTERS_OUTPUT = "\tfield %s -> reads: %d writes: %d";

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify the class name that contains a main method to analyse.");
            return;
        }
        String className = args[0];

        String[] arguments = new String[args.length - 1];

        System.arraycopy(args, 1, arguments, 0, arguments.length);

        ClassPool pool = ClassPool.getDefault();

        Translator translator = new ProfilerTranslator();

        Loader classLoader = new Loader();
        classLoader.delegateLoadingOf("ist.meic.pa.FunctionalProfilerExtended.Register");

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

        System.out.println(String.format(TOTAL_READS_WRITES, Register.getTotalReader(), Register.getTotalWriter()));
        Register.getCounters().forEach(WithFunctionalProfiler::printOutput);
    }

    private static void printOutput(String className, Set<FieldInfo> fieldInfos) {
        FieldInfo fields = fieldInfos
                .stream()
                .reduce(new FieldInfo("allFieldsInThisClass", 0, 0), ((fieldInfo, fieldInfo2) -> {
                    fieldInfo.setReader(fieldInfo.getReader() + fieldInfo2.getReader());
                    fieldInfo.setWriter(fieldInfo.getWriter() + fieldInfo2.getWriter());
                    return fieldInfo;
                }));
        System.out.println(String.format(CLASS_COUNTERS_OUTPUT, className, fields.getReader(), fields.getWriter()));

        fieldInfos
                .forEach(fieldInfo ->
                        System.out.println(String.format(FIELD_COUNTERS_OUTPUT, fieldInfo.getName(), fieldInfo.getReader(), fieldInfo.getWriter())))
        ;
    }
}