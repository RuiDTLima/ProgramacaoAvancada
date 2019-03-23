package ist.meic.pa.FunctionalProfilerExtended;

import java.util.Objects;

/**
 * This class represents a counter for reads and writes. It is used one instance for every class.
 */
public class FieldInfo {
    private final String name;
    private int reader;
    private int writer;

    public FieldInfo(String name, int reader, int writer) {
        this.name = name;
        this.reader = reader;
        this.writer = writer;
    }

    public String getName() {
        return name;
    }

    public int getReader() {
        return reader;
    }

    public int getWriter() {
        return writer;
    }

    public void setReader(int reader) {
        this.reader = reader;
    }

    public void setWriter(int writer) {
        this.writer = writer;
    }

    public void incrementReader() {
        reader++;
    }

    public void incrementWriter() {
        writer++;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || this.getClass() != object.getClass()) return false;
        FieldInfo fieldInfo = (FieldInfo) object;
        return this.name.equals(fieldInfo.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
