package ist.meic.pa.FunctionalProfiler;

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
                System.out.println("Write in if.");
                //i = i + 1;
            }
            System.out.println("write out of if");
            i = i + 1;
        }
        return this;
    }
}
