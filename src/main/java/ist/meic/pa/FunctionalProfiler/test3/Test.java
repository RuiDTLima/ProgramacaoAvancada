package ist.meic.pa.FunctionalProfiler.test3;

public class Test {

    public static void main(String[] args) {
        Professor p = new Professor();
        p.grade(new Student());
    }

    // Expected:
    // Total reads 0 Total writes 2
    // (package name).Student -> reads 0 writes 2
}
