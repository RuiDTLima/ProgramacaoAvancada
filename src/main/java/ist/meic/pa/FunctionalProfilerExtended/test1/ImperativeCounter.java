package ist.meic.pa.FunctionalProfilerExtended.test1;

public class ImperativeCounter implements Counter {
    int i;
    int xpto;

    ImperativeCounter(int start) {
        i = start;
    }

    public int value() {
        return i;
    }

    public Counter advance() {
        xpto = 0;
        for (int j = 0; j < 10; j++) {
            if (j % 2 == 0) {
                xpto = j;
                i = i + 1;
            }
            i = i + 1;
        }
        return this;
    }
}
