package code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CSVGenerator {
    public int totalTas = 0;
    public int totalStudents = 0;
    ArrayList<String> courses = new ArrayList<>();
    ArrayList<Integer> students = new ArrayList<>();


    public void generateStudents() {
        Random rand = new Random();

        totalStudents =  totalTas;

        try {
            FileWriter file = new FileWriter("files/students.csv");

            for (int i = 0; i < totalStudents; i++) {
                int Am = (rand.nextInt(9000) + 1000);
                while (students.contains(Am)) {
                    Am = (rand.nextInt(9000) + 1000);
                }

                students.add(Am);
                file.write("Onoma" + i + ", Epitheto" + i + ", " + Am + "\n");
            }

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateCourses() {
        Random random = new Random();

        int totalCourses = random.nextInt(10) + 5;

        try {
            FileWriter file = new FileWriter("files/courses.csv");

            for (int i = 0; i < totalCourses; i++) {
                int tas = random.nextInt(10) + 1;
                totalTas += tas;
                file.write("C" + i + ", " + tas + "\n");
                courses.add("C" + i);
            }

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateStudentGrades() {
        try {
            FileWriter file = new FileWriter("files/studentGrades.csv");

            for (int i = 0; i < totalStudents; i++) {
                Random rand = new Random();

                int studentCourses = courses.size();

                for (int j = 0; j < studentCourses; j++) {
                    float grade = rand.nextInt(6) + 5 + rand.nextInt(2) * 0.5F;
                    if (grade > 10) {
                        grade = 10;
                    }
                    file.write("C" + j + ", " + students.get(i) + ", " + grade + "\n");
                }
            }

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
