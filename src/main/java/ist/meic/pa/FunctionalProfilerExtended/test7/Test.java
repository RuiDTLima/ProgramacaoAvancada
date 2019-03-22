package ist.meic.pa.FunctionalProfilerExtended.test7;

public class Test {

    public static void main(String[] args) {
        Student student = new Student();
        Professor p = new Professor();
        p.grade(student);
    }

    // Expected:
    // Total reads 2 Total writes 2
    // (package name).Professor -> reads 2 writes 0
    // (package name).Student -> reads 0 writes 2
}
