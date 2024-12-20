import React, { useEffect, useState } from "react";
import axios from "axios";
import Box from '@mui/material/Box';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import TableSortLabel from '@mui/material/TableSortLabel';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';

const Courses = () => {
    const [courses, setCourses] = useState([]);
    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('courseName');

    useEffect(() => {
        axios
            .get("http://localhost:8080/api/courses")
            .then((response) => {
                setCourses(response.data);
            })
            .catch((error) => console.error("Error fetching courses data:", error));
    }, []);

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };

    const sortedCourses = React.useMemo(() => {
        return [...courses].sort((a, b) => {
            const aValue = a[orderBy];
            const bValue = b[orderBy];

            if (typeof aValue === "number" && typeof bValue === "number") {
                return order === "asc" ? aValue - bValue : bValue - aValue;
            } else {
                return order === "asc"
                    ? (aValue || "").toString().localeCompare((bValue || "").toString())
                    : (bValue || "").toString().localeCompare((aValue || "").toString());
            }
        });
    }, [courses, order, orderBy]);

    return (
        <div className={"container-fluid d-flex justify-content-center align-items-center"}>
            <Box sx={{ width: '80%' }}>
                <Paper sx={{ width: '100%', mb: 2 }}>
                    <Box sx={{ padding: 2 }}>
                        <Typography variant="h6" component="div">
                            Courses
                        </Typography>
                    </Box>
                    <TableContainer>
                        <Table stickyHeader>
                            <TableHead>
                                <TableRow>
                                    <TableCell>
                                        <TableSortLabel
                                            active={orderBy === 'courseName'}
                                            direction={orderBy === 'courseName' ? order : 'asc'}
                                            onClick={(event) => handleRequestSort(event, 'courseName')}
                                        >
                                            Course Name
                                        </TableSortLabel>
                                    </TableCell>
                                    <TableCell>
                                        <TableSortLabel
                                            active={orderBy === 'assistants'}
                                            direction={orderBy === 'assistants' ? order : 'asc'}
                                            onClick={(event) => handleRequestSort(event, 'assistants')}
                                        >
                                            Assistants
                                        </TableSortLabel>
                                    </TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sortedCourses.map((course, index) => (
                                    <TableRow
                                        key={index}
                                        hover
                                        sx={{
                                            cursor: "pointer",
                                            '&:hover': {
                                                backgroundColor: 'rgba(0, 0, 0, 0.04)', // Optional: Custom hover color
                                            },
                                        }}
                                    >
                                        <TableCell>{course.courseName}</TableCell>
                                        <TableCell>{course.assistants}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </Paper>
            </Box>
        </div>
    );
};

export default Courses;
