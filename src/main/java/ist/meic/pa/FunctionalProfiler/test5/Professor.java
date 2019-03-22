package ist.meic.pa.FunctionalProfiler.test5;

public class Professor {
    private String course;

    // Com o construtor é outro caso de teste
    public Professor(Student t) {
        t.mark = 15; // Mark deve ser contado como uma escrita na classe student
        this.course = "Advanced Programming" + this.course; // + this.course deve ser contado como uma leitura
    }

    void grade(Student t) {
        t.mark = 20;
    }
}
