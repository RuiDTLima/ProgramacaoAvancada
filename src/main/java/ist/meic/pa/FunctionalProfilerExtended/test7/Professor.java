package ist.meic.pa.FunctionalProfilerExtended.test7;

import ist.meic.pa.FunctionalProfilerExtended.IgnoreInstrumentation;

public class Professor {
    private String course;
    private Student student;

    // Com o construtor é outro caso de teste
    public Professor() {
        this.student = new Student();
        this.student.mark = 15; // Mark deve ser contado como uma escrita na classe student
        this.course = "Advanced Programming" + this.course; // + this.course deve ser contado como uma leitura
    }

    void grade(Student t) {
        t.mark = 20;
    }
}
