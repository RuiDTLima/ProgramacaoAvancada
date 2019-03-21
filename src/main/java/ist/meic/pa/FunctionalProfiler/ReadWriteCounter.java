package ist.meic.pa.FunctionalProfiler;

public class ReadWriteCounter {
    private int reader;
    private int writer;

    public ReadWriteCounter() {
        this.reader = 0;
        this.writer = 0;
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
