package ist.meic.pa.FunctionalProfiler;

/**
 * This class represents a counter for reads and writes. It is used one instance for every class.
 */
public class FieldInfo {
    private int reader;
    private int writer;

    public FieldInfo(int reader, int writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public int getReader() {
        return reader;
    }

    public int getWriter() {
        return writer;
    }

    public void incrementReader() {
        reader++;
    }

    public void incrementWriter() {
        writer++;
    }
}
