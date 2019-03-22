package ist.meic.pa.FunctionalProfiler.test7;

public class Test {

    public static void main(String[] args) {
        Student student = new Student();
        Professor p = new Professor(student);
        p.grade(student);
    }

    // Expected:
    // Total reads 1 Total writes 2
    // (package name).Professor -> reads 1 writes 0
    // (package name).Student -> reads 0 writes 2
}
