package ist.meic.pa.FunctionalProfiler;

import java.util.SortedMap;
import java.util.TreeMap;

public class Register {
    private final static SortedMap<String, ReadWriteCounter> counters = new TreeMap<>();
    private static int totalReader = 0, totalWriter = 0;

    public static SortedMap<String, ReadWriteCounter> getCounters() {
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
