package ist.meic.pa.FunctionalProfilerExtended;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This maintains a register of all FieldInfo corresponding to all classes as well as the total reads and writes.
 */
public class Register {
    /**
     * It is used a TreeMap so that all the keys are order lexicographic.
     */
    private static final SortedMap<String, Set<FieldInfo>> counters = new TreeMap<>();
    private static int totalReader = 0, totalWriter = 0;

    public static SortedMap<String, Set<FieldInfo>> getCounters() {
        return counters;
    }

    public static int getTotalReader() {
        return totalReader;
    }

    public static int getTotalWriter() {
        return totalWriter;
    }

    public static void addReader(String className, String fieldName) {
        if (counters.containsKey(className)) {
            getFieldInfo(counters.get(className), fieldName).incrementReader();
        } else {
            Set<FieldInfo> fieldInfos = new HashSet<>();
            fieldInfos.add(new FieldInfo(fieldName, 1, 0));
            counters.put(className, fieldInfos);
        }
        totalReader++;
    }

    public static void addWriter(String className, String fieldName) {
        if (counters.containsKey(className)) {
            getFieldInfo(counters.get(className), fieldName).incrementWriter();
        } else {
            Set<FieldInfo> fieldInfos = new HashSet<>();
            fieldInfos.add(new FieldInfo(fieldName, 0, 1));
            counters.put(className, fieldInfos);
        }
        totalWriter++;
    }

    private static FieldInfo getFieldInfo(Set<FieldInfo> fieldInfos, String fieldName) {
        return fieldInfos
                .stream()
                .filter(fieldInfo -> fieldInfo.getName().equals(fieldName))
                .findFirst()
                .orElseGet(() -> {
                    FieldInfo fieldInfo = new FieldInfo(fieldName, 0, 0);
                    fieldInfos.add(fieldInfo);
                    return fieldInfo;
                });
    }
}
