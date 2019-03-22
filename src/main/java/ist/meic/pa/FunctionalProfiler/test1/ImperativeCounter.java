package ist.meic.pa.FunctionalProfiler.test1;

public class ImperativeCounter implements Counter {
    int i;

    ImperativeCounter(int start) {
        i = start;
    }

    public int value() {
        return i;
    }

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
