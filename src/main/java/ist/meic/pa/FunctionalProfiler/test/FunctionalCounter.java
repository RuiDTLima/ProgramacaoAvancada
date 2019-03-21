package ist.meic.pa.FunctionalProfiler.test;

public class FunctionalCounter extends  abc implements Counter {
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

    @Override
    public void a() {
        i = i + 1;
    }
}
