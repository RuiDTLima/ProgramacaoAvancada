package ist.meic.pa.FunctionalProfiler;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This maintains a register of all FieldInfo corresponding to all classes as well as the total reads and writes.
 */
public class Register {
    /**
     * It is used a TreeMap so that all the keys are order lexicographic.
     */
    private static final SortedMap<String, FieldInfo> counters = new TreeMap<>();
    private static int totalReader = 0, totalWriter = 0;

    public static SortedMap<String, FieldInfo> getCounters() {
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
            counters.put(className, new FieldInfo(1, 0));
        totalReader++;
    }

    public static void addWriter(String className) {
        if (counters.containsKey(className))
            counters.get(className).incrementWriter();
        else
            counters.put(className, new FieldInfo(0, 1));
        totalWriter++;
    }
}
