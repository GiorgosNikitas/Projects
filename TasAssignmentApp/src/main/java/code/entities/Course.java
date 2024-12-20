package code.entities;

import org.json.JSONObject;

import java.util.ArrayList;

public class Course {
    private String courseName;
    private float tas;
    public ArrayList<Student> assignedTas;
    public ArrayList<Student> bannedStudents;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getTas() {
        return tas;
    }

    public void setTas(float tas) {
        this.tas = tas;
    }

    public ArrayList<Student> getAssignedTas() {
        return assignedTas;
    }

    public void banStudent(Student student) {
        if (bannedStudents.contains(student)) {
            System.err.println("Student: " + student.getFirstName() + " " + student.getLastName() + " is already banned");
        } else {
            bannedStudents.add(student);
        }
    }

    public ArrayList<Student> getBannedStudents() {
        return bannedStudents;
    }

    public Course(String courseName, float assistants) {
        this.courseName = courseName;
        this.tas = assistants;
        assignedTas = new ArrayList<>();
        bannedStudents = new ArrayList<>();
    }

    public float tasCourseMeanGrade() {
        float sum = 0;
        for (Student student : assignedTas) {
            sum += student.courseGrades.get(courseName);
        }
        return sum / assignedTas.size();
    }

    public void assignTa(Student student) {
        assignedTas.add(student);
        if (assignedTas.size() > tas) {
            System.err.println("Too many assistants assigned!");
        }
    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }
}
