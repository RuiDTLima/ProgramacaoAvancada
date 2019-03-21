package ist.meic.pa.FunctionalProfiler.test;

public class Example {
    public static void test(Counter c1, Counter c2) {
        System.out.println(String.format("%s %s", c1.value(), c2.value()));
    }

    public static void main(String[] args) {
        Counter fc = new FunctionalCounter(0);
        ((FunctionalCounter) fc).a();
        ((FunctionalCounter) fc).b();
        test(fc, fc.advance());
        Counter ic = new ImperativeCounter(0);
        test(ic, ic.advance());
    }
}
