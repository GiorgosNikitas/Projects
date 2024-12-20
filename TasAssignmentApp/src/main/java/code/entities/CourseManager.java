package code.entities;

import code.entities.DTO.CourseDTO;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class CourseManager {
    public static ArrayList<Course> courses;
    public int totalCourses = 0;
    public static float totalTas = 0;

    public CourseManager() {
       courses = new ArrayList<>();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public int getTotalCourses() {
        return totalCourses;
    }

    @PostMapping("/uploadassistants")
    public ResponseEntity<String> uploadCourseFile(@RequestParam("file") MultipartFile file,
                                                 @RequestParam("fileName") String fileName) {
        try {
            String uploadDir = System.getProperty("java.io.tmpdir");
            File destinationFile = new File(uploadDir, fileName);
            file.transferTo(destinationFile);

            System.out.println("File uploaded successfully to: " + destinationFile.getAbsolutePath());
            readCourses(destinationFile.getAbsolutePath());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload file!");
        }
    }

    public void readCourses(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                if (values.length != 2) {
                    System.err.println("Bad csv!");
                }
                float tas = Float.parseFloat(values[1]);
                totalTas += tas;
                courses.add(new Course(values[0], tas));
            }
            totalCourses = courses.size();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printCourses() {
        System.out.println("----Courses----");
        for (Course course : courses) {
            System.out.println(course.getCourseName() + "\t" + course.getTas());
        }
    }

    public static Course getCourseByName(String name) {
        for (Course course : courses) {
            if (course.getCourseName().equals(name)) {
                return course;
            }
        }

        return null;
    }

    public void printResults() {
        for (Course course : courses) {
            System.out.println(course.getCourseName() + "\tTotal assistants = " + course.getTas());
            for (int i = 0; i < course.getTas(); i++) {
                System.out.println(course.assignedTas.get(i).toString(course.getCourseName()));
            }
            System.out.println("********************");
        }
    }

    public JSONObject toJSON() {
        JSONObject json =  new JSONObject(this);

        System.out.println(json);
        try {
            FileWriter file = new FileWriter("files/courses.json");
            file.write(json.toString());
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    @GetMapping("/courses")
    public List<CourseDTO> getCoursesJSON() {
        return this.getCourses().stream()
                .map(course -> new CourseDTO(course.getCourseName(), course.getTas()))
                .collect(Collectors.toList());
    }
}
