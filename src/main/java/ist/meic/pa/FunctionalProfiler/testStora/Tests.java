package ist.meic.pa.FunctionalProfiler.testStora;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Tests {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: Tests test1 ... testn");
            System.exit(1);
        }

        for (String test : args) {
            try {
                Method m = Tests.class.getDeclaredMethod(test);
                m.invoke(null);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                // Should not happen
                System.out.println("Test method does not exist: " + test);
            }
        }
    }

    static void test1() {
        new Person();
    }

    static void test2() {
        new Person().selfIntroduce();
    }

    static void test3() {
        new Person().switchName("Foo");
    }

    static void test4() {
        new Person().firstname += "Bar";
    }

    static void test5() {
        Person p = new Person();
        for (int i = 0; i < 5; i++) {
            p.celebrateBirthday();
        }

        while (p.age < 70) {
            p.celebrateBirthday();
        }
    }

    static void test6() {
        new Person("Foo", "Bar");
    }

    static void test7() {
        String s = new Person("Foo", "Bar").firstname + new Person("Foo", "Bar").surname + "new Person(\"Foo\", \"Bar\").surname";
    }

    static void test8() {
        new Person(new Person());
    }

    static void test9() {
        new Person(new Student("Harry", "Potter"));
    }

    static void test10() {
        new Student();
    }

    static void test11() {
        Student t = new Student();
        new Professor().grade(t);
        new Professor().grade(t);

    }

    static void test12() {
        Professor professor = new Professor();
        Student student = new Student();
        Person person = new Person();
        professor.firstname = "Prof";
        student.firstname = "Student";
        person.firstname = "Person";
    }

    static void test13() {
        Professor p = new Professor();
        p.grade(new StudentPAva());
    }

    static void test14() {
        ist.meic.pa.FunctionalProfiler.testStora.sampleC.Example.test();
        ist.meic.pa.FunctionalProfiler.testStora.sampleA.Example.test();
        ist.meic.pa.FunctionalProfiler.testStora.sample0.Example.test();
    }

    static void test15() {
        Car car = new Car(10);
    }

    static void test16() {
        Car car = new Car();
    }

    static void test17() {
        try (MyFakeFileWriter in = new MyFakeFileWriter("WingardiumLeviosa_ForBeginners.pdf")) {
            in.readLine();
        } catch (IOException e) {
            // Do you really think a fake file writer is so powerful that would throw an exception ? O:
        }
    }

    static void test18() {
        for (int i = 1; i < 18; i++) {
            try {
                Method m = Tests.class.getDeclaredMethod("test" + i);
                m.invoke(null);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                // Should not happen
                System.out.println("Test method does not exist: test" + i);
            }
        }
    }
}

// Test Classes
class Person {
    String firstname;
    String surname;

    int age;

    Person() {
    }

    Person(String firstname, String lastname) {
        this.firstname = firstname;
        this.surname += lastname;
    }

    Person(Person p) {
        this.firstname = p.firstname;
        p.surname = "";
        this.surname = p.surname;
    }

    Person(Student t) {
        this.firstname = t.firstname;
        t.firstname = "Ron";
        this.surname = t.surname;
        t.age = this.age + 20;
    }

    String selfIntroduce() {
        return this.firstname + " " + this.surname;
    }

    void switchName(String name) {
        this.firstname = name;
    }

    void celebrateBirthday() {
        this.age += 1;
    }
}

class Professor extends Person {

    void grade(Student t) {
        t.mark = 20;
        t.addFavorite(this);
    }

    void grade(StudentPAva tp) {
        tp.mark = 20;
        tp.addFavorite(this);
        tp.pass = true;
    }
}

class Student extends Person {
    int mark;
    Professor[] favoriteProfessors;

    Student() {
        this.favoriteProfessors = new Professor[5];
    }


    Student(String firstname, String lastname) {
        this.firstname = firstname;
        this.surname = lastname;
    }

    Student(Student other) {
        Student otherThis = this;
        otherThis.firstname = "Foo";
        other.firstname = "Foo";
        otherThis.surname = "Bar";
        other.age = 0;
        other.favoriteProfessors = new Professor[5];
    }

    void addFavorite(Professor p) {
        if (favoriteProfessors != null) {
            int i = 0;
            for (; i < 5 && favoriteProfessors[i] != null; i++) ;
            if (i < 5) favoriteProfessors[i] = p;
        }
    }
}

class StudentPAva extends Student {
    boolean pass = false;
}


// Test Classes
class Car {
    int maxSpeed;
    float fuelCapacity;

    Car(float fuelCapacity) {
        this.maxSpeed = 15;
        this.fuelCapacity = this.fuelCapacity + fuelCapacity;
        this.raiseMaxSpeed(200); // I wanna move faster than a snail, for god's sake...
    }

    Car() {
        this.maxSpeed = 20;
        this.fuelCapacity = 6;

        new Motor(this).turnOn();
    }

    void raiseMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    static class Motor {
        boolean on;
        int maxSpeed;

        Motor(Car c) {
            on = false;
            maxSpeed = c.maxSpeed;
            c.maxSpeed = 200;
        }

        private void turnOn() {
            on = true;
            maxSpeed *= 2;
        }
    }
}

class MyFakeFileWriter implements AutoCloseable {
    String in;

    MyFakeFileWriter(String filename) {
        this.in += filename;
        String s = "Opening file this.filename"; // Oops! What now?
    }

    String readLine() {
        return "The first and foremost important aspect of Wingardium Leviosa is to learn the proper accent! Try with me: *wingaaardiuum* *lééviosa*.";
    }

    @Override
    public void close() throws IOException {
        this.in = "";
    }
}