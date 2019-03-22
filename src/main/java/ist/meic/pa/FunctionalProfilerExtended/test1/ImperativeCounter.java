package ist.meic.pa.FunctionalProfilerExtended.test1;

import ist.meic.pa.FunctionalProfilerExtended.IgnoreInstrumentation;

public class ImperativeCounter implements Counter {
    int i;

    ImperativeCounter(int start) {
        i = start;
    }

    public int value() {
        return i;
    }

    @IgnoreInstrumentation
    public Counter advance() {
        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                i = i + 1;
            }
            i = i + 1;
        }
        return this;
    }
}
