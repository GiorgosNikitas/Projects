package code.entities;

import code.entities.DTO.StudentDTO;
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
public class StudentManager {
    public static ArrayList<Student> students;
    private int totalStudents;

    public ArrayList<Student> getStudents() {
        return students;
    }

    public StudentManager() {
        students = new ArrayList<>();
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    @PostMapping("/uploadstudents")
    public ResponseEntity<String> uploadStudentsFile(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("fileName") String fileName) {
        try {
            String uploadDir = System.getProperty("java.io.tmpdir");
            File destinationFile = new File(uploadDir, fileName);
            file.transferTo(destinationFile);

            System.out.println("File uploaded successfully to: " + destinationFile.getAbsolutePath());
            readStudents(destinationFile.getAbsolutePath());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload file!");
        }
    }

    public void readStudents(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                if (values.length != 3) {
                    System.err.println("Bad csv!");
                }
                students.add(new Student(Integer.parseInt(values[2]), values[0], values[1]));
            }
            totalStudents = students.size();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/uploadgrades")
    public ResponseEntity<String> uploadGradesFile(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("fileName") String fileName) {
        try {
            String uploadDir = System.getProperty("java.io.tmpdir");
            File destinationFile = new File(uploadDir, fileName);
            file.transferTo(destinationFile);

            System.out.println("File uploaded successfully to: " + destinationFile.getAbsolutePath());
            readGrades(destinationFile.getAbsolutePath());
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to upload file!");
        }
    }

    public void readGrades(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim();
                }
                if (values.length != 3) {
                    System.err.println("Bad csv!");
                }
                getStudentByAM(Integer.parseInt(values[1])).courseGrades.put(values[0], Float.parseFloat(values[2]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printStudents() {
        System.out.println("----Students----");
        for (Student student : students) {
            System.out.println(student.toString());
        }
    }

    public void printStudentGrades() {
        for (Student student : students) {
            student.printCourseGrades();
        }
    }

    public Student getStudentByAM(int AM) {
        for (Student student : students) {
            if (student.getAm() == AM) {
                return student;
            }
        }

        System.out.println("Student with AM: " + AM + " not found!");
        return null;
    }

    public ArrayList<Student> getStudentsByFirstname(String firstname) {
        ArrayList<Student> temp = new ArrayList<>();
        for (Student student : students) {
            if (student.getFirstName().equals(firstname)) {
                temp.add(student);
            }
        }

        return temp;
    }

    public ArrayList<Student> getStudentsByLastname(String lastName) {
        ArrayList<Student> temp = new ArrayList<>();
        for (Student student : students) {
            if (student.getLastName().equals(lastName)) {
                temp.add(student);
            }
        }

        return temp;
    }

    public JSONObject toJSON() {
        JSONObject json =  new JSONObject(this);

        System.out.println(json);
        try {
            FileWriter file = new FileWriter("files/students.json");
            file.write(json.toString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return json;
    }

    @GetMapping("/students")
    public List<StudentDTO> getStudentsJSON() {
        return this.getStudents().stream()
                .map(student -> new StudentDTO(student.getFirstName(), student.getLastName(), String.valueOf(student.getAm()), student.courseGrades))
                .collect(Collectors.toList());
    }
}
