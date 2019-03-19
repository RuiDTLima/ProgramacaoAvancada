package ist.meic.pa.FunctionalProfiler;

public class FunctionalCounter implements Counter {
    int i;

    public FunctionalCounter(int start) {
        i = start;
    }

    public int value() {
        return i;
    }

    public Counter advance() {
        return new FunctionalCounter(i + 1);
    }
}
