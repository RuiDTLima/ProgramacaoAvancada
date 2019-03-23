package ist.meic.pa.FunctionalProfiler.testStora.sample0;

interface Counter {
    public int value();
    public Counter advance();
}

class ImperativeCounter implements Counter {
    int i;

    ImperativeCounter(int start) {
	i = start;
    }
    public int value() {
	return i;
    }
    public Counter advance() {
	i = i+1;
	return this;
    }
}

class FunctionalCounter implements Counter {
    int i;

    public FunctionalCounter(int start) {
	i = start;
    }
    public int value() {
	return i;
    }
    public Counter advance() {
	return new FunctionalCounter(i+1);
    }
}

public class Example {
    public static void test() {
        Counter c1 = new FunctionalCounter(0);
        test(c1, c1.advance());
        Counter c2 = new ImperativeCounter(0);
        test(c2, c2.advance());
    }
    public static void test(Counter c1, Counter c2) {
        String.format("%s %s", c1.value(), c2.value());
    }
}
