package ist.meic.pa.FunctionalProfiler;

public class ReadWriteCounter {
    private int reader;
    private int writer;

    public ReadWriteCounter(int reader, int writer) {
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
