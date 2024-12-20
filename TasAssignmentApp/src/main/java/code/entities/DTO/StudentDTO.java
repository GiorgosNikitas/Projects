package code.entities.DTO;

import java.util.HashMap;

public class StudentDTO {
    private String firstName;
    private String lastName;
    private String am;
    private HashMap<String, Float> courseGrades;

    public StudentDTO(String firstName, String lastName, String am, HashMap<String, Float> courseGrades) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.am = am;
        this.courseGrades = courseGrades;
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

    public String getAm() {
        return am;
    }

    public HashMap<String, Float> getCourseGrades() {
        return courseGrades;
    }

    public void setCourseGrades(HashMap<String, Float> courseGrades) {
        this.courseGrades = courseGrades;
    }

    public void setAm(String am) {
        this.am = am;
    }
}
