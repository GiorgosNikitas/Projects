package code;

import code.entities.CourseManager;
import code.entities.StudentManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

//        CSVParser parser = new CSVParser();
//        parser.generateCourses();
//        parser.generateStudents();
//        parser.generateStudentGrades();
//        System.out.println("Tas needed: " + parser.totalTas);
//        System.out.println("Students: " + parser.totalStudents);
//        System.out.println(parser.students.size());

        CourseManager courseManager = new CourseManager();
//        courseManager.readCourses();
//        courseManager.printCourses();

        StudentManager studentManager = new StudentManager();
//        studentManager.readStudents();
//        studentManager.printStudents();

//        studentManager.readGrades();
//        studentManager.printStudentGrades();

        long start = System.nanoTime();

//        Hungarian hungarian = new Hungarian();
//        hungarian.execute();

        long finish = System.nanoTime();
        long timeElapsed = finish - start;

//        courseManager.printResults();

        System.out.println(timeElapsed/1000000000D);
    }

}
