import React, { useEffect, useState } from "react";
import axios from "axios";
import {Button, Card, Collapse} from "react-bootstrap";

const Students = () => {
    const [students, setStudents] = useState([]);
    const [open, setOpen] = useState({});

    useEffect(() => {
        axios
            .get("http://localhost:8080/api/students")
            .then((response) => {
                setStudents(response.data);
            })
            .catch((error) => console.error("Error fetching courses data:", error));
    }, []);

    const toggleCollapse = (index) => {
        setOpen((prevState) => ({...prevState,
            [index]: !prevState[index]
        }));
    };

    return (
        <div className={"container-flex"}>
            <div className={"row"}>
                <div className={"col"}>
                    <h4>First Name</h4>
                </div>
                <div className={"col"}>
                    <h4>Last Name</h4>
                </div>
                <div className={"col"}>
                    <h4>AM</h4>
                </div>
            </div>
            {students.map((student, index) => (
                <div key={index} className={"container-fluid"}>
                    <div className={"row"}>
                        <div className={"col"}>
                            <p>{student.firstName}</p>
                        </div>
                        <div className={"col"}>
                            <p>{student.lastName}</p>
                        </div>
                        <div className={"col"}>
                            <p>{student.am}</p>
                        </div>
                        <div className={"col"}>
                            <p>
                                <Button onClick={() => toggleCollapse(index)} aria-controls={`collapse-${index}`}
                                        aria-expanded={open[index] ? "true" : "false"}>
                                    Course Grades
                                </Button>
                            </p>
                        </div>
                    </div>

                    <Collapse in={open[index]}>
                        <div id={`collapse-${index}`}>
                            <Card>
                                <Card.Body>
                                    <table border="1" style={{width: "100%", textAlign: "left"}}>
                                        <thead>
                                        <tr>
                                            <th>Course</th>
                                            <th>Grade</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            {Object.entries(student.courseGrades).map(([course, grade]) => (
                                                <tr key={"index"}>
                                                    <td>
                                                        {course}
                                                    </td>
                                                    <td>
                                                        {grade}
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </Card.Body>
                            </Card>
                        </div>
                    </Collapse>
                </div>
                ))}
        </div>
    );
};

export default Students;