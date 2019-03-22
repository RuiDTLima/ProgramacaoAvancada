package ist.meic.pa.FunctionalProfiler.test6;

public class Student extends Person {
    int mark;

    public void setMark(int mark) {
        this.mark = mark;
    }

    public Student() {
        this.setAge(22);
        this.mark = 15;
    }
}
