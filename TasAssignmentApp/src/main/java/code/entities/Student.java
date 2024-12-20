package code.entities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private int am;
    private String firstName;
    private String lastName;
    public HashMap<String, Float> courseGrades;

    public int getAm() {
        return am;
    }

    public void setAm(int am) {
        this.am = am;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public HashMap<String, Float> getCourseGrades() {
        return courseGrades;
    }

    public Student(int am, String firstName, String lastName) {
        this.am = am;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseGrades = new HashMap<>();
    }

    public Student(int am, String firstName, String lastName, HashMap<String, Float> courseGrades) {
        this.am = am;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courseGrades = new HashMap<>(courseGrades);
    }

    public float meanOfGrades() {
        float sum = 0;
        for (Map.Entry<String, Float> course : courseGrades.entrySet()) {
            sum += course.getValue();
        }

        return sum / courseGrades.size();
    }

    public void printCourseGrades() {
        System.out.println(this.toString());
        for (String key : this.courseGrades.keySet()) {
            System.out.println(key + "\t" + this.courseGrades.get(key));
        }
        System.out.println("MO: " + meanOfGrades() + "\n");
    }

    public String toString() {
        return Integer.toString(am) + "\t" + firstName + "\t" + lastName;
    }

    public String toString(String courseName) {
        return Integer.toString(am) + "\t" + firstName + "\t" + lastName + "\tGrade: " + courseGrades.get(courseName);
    }

    public JSONObject toJSON() {
        return new JSONObject(this);
    }
}