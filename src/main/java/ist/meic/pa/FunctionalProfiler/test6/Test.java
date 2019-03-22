package ist.meic.pa.FunctionalProfiler.test6;

public class Test {

    public static void main(String[] args) {
        Professor p = new Professor();
        p.grade(new Student());
    }

    // Expected:
    // Total reads 0 Total writes 3
    // (package name).Student -> reads 0 writes 3
}
