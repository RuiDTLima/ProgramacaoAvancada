package ist.meic.pa.FunctionalProfiler;

import java.util.HashMap;

public class Register {
    private final static HashMap<String, ReadWriteCounter> counters = new HashMap<>();
    private static int totalReader = 0, totalWriter = 0;

    public static HashMap<String, ReadWriteCounter> getCounters() {
        return counters;
    }

    public static int getTotalReader() {
        return totalReader;
    }

    public static int getTotalWriter() {
        return totalWriter;
    }

    public static void addReader(String className) {
        if (counters.containsKey(className))
            counters.get(className).incrementReader();
        else
            counters.put(className, new ReadWriteCounter(1, 0));
        totalReader++;
    }


    public static void addWriter(String className) {
        if (counters.containsKey(className))
            counters.get(className).incrementWriter();
        else
            counters.put(className, new ReadWriteCounter(0, 1));
        totalWriter++;
    }
}
