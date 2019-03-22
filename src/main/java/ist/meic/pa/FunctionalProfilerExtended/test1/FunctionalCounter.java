package ist.meic.pa.FunctionalProfilerExtended.test1;

import ist.meic.pa.FunctionalProfilerExtended.IgnoreInstrumentation;

public class FunctionalCounter extends abc implements Counter {
    @IgnoreInstrumentation
    int i;

    @IgnoreInstrumentation
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
//