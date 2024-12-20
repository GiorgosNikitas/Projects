package code.entities.DTO;

public class CourseDTO {
    private String courseName;
    private float assistants;

    // Constructor
    public CourseDTO(String courseName, float assistants) {
        this.courseName = courseName;
        this.assistants = assistants;
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public float getAssistants() {
        return assistants;
    }

    public void setAssistants(float assistants) {
        this.assistants = assistants;
    }
}
