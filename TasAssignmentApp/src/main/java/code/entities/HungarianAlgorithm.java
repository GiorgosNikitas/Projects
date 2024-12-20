package code.entities;

import java.util.ArrayList;
import java.util.Arrays;

public class HungarianAlgorithm {
    private final float[][] costs;
    private float[][] matrix;
    private int[][] markedMatrix;
    private final int totalStudents;
    private final int totalTasks;
    private final String[] taskToCourse;

    public static void main(String[] args) {
        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm();

        hungarianAlgorithm.execute();
    }

    public HungarianAlgorithm() {
        totalTasks = (int)CourseManager.totalTas;
        totalStudents = StudentManager.students.size();
        costs = new float[totalStudents][totalTasks];
        taskToCourse = new String[totalTasks]; // !!!!!!!!!!!!!!!

        int current = 0;

        // for each course
        for (Student student : StudentManager.students) {
            int currentCourse = 0;
            ArrayList<Float> taskCost = new ArrayList<>();
            for (Course course : CourseManager.courses) {
                String courseName = course.getCourseName();
                float grade;
                if (student.courseGrades.get(courseName) != null) {
                    grade = student.courseGrades.get(courseName);
                } else {
                    grade = 0;
                }
                // Set very high cost (10) for banned students, so that
                // they will not be picked by the algorithm
                for (int i = 0; i < course.getTas(); i++) {
                    if (course.bannedStudents.contains(student)) {
                        taskCost.add(5F);
                    } else {
                        taskCost.add(10 - grade);
                    }

                    taskToCourse[currentCourse] = course.getCourseName();
                    currentCourse++;
                }
            }

            // for how many TAs each course needs
            int taskCounter = 0;
            for (int i = 0; i < CourseManager.courses.size(); i++) {
                for (int j = 0; j < (int) CourseManager.courses.get(i).getTas(); j++) {
                    costs[current][taskCounter] = taskCost.get(taskCounter);
                    taskCounter++;
                }
            }
            current++;
        }
    }

    public void execute() {
//        costs = new float[][]{{0.5f, 2.5f, 1f, 2.5f},
//                              {0, 2.5f, 2.5f, 3f},
//                              {1f, 4f, 2.5f, 5f},
//                              {0, 2, 5, 4}};
//
//        totalStudents = costs.length;
//        totalTasks = costs[0].length;

        matrix = new float[totalStudents][totalTasks];
        for (int i = 0; i < totalStudents; i++) {
            System.arraycopy(costs[i], 0, matrix[i], 0, totalTasks);
        }

        // step 1
        reduceRows();

        // step 2
        reduceCols();

        // step 3
        markedMatrix = new int[totalStudents][totalTasks]; // 1 -> starred zeroes, 2 -> primed zeroes
        for (int i = 0; i < totalStudents; i++) {
            Arrays.fill(markedMatrix[i], 0);
        }

        int[] starredRows = new int[totalStudents];
        Arrays.fill(starredRows, 0);

        int[] starredCols = new int[totalTasks];
        Arrays.fill(starredCols, 0);

        // step 4
        int[] coveredRows = new int[totalStudents];
        int[] coveredCols = new int[totalStudents];

        for (int i = 0; i < totalStudents; i++) {
            Arrays.fill(markedMatrix[i], 0);
        }

        Arrays.fill(starredRows, 0);

        Arrays.fill(starredCols, 0);

        Arrays.fill(coveredRows, 0);
        Arrays.fill(coveredCols, 0);

        while (starCount() < totalTasks) {
            for (int i = 0; i < totalStudents; i++) {
                for (int j = 0; j < totalTasks; j++) {
                    if (matrix[i][j] == 0 && markedMatrix[i][j] == 0 && starredRows[i] == 0 && starredCols[j] == 0) {
                        markedMatrix[i][j] = 1;
                        starredRows[i] = 1;
                        starredCols[j] = 1;
                        break;
                    }
                }
            }

            // Cover all columns containing a starred zero
            for (int j = 0; j < totalTasks; j++) {
                if (starInCol(j)) {
                    coveredCols[j] = 1;
                }
            }

            boolean exitLoop = true;
            int j = 0;
            int i = 0;

            while (zeroExists(coveredRows, coveredCols)) {
                for (int x = 0; x < totalTasks; x++) {
                    if (starInCol(x)) {
                        coveredCols[x] = 1;
                    }
                }
                boolean change = true;
                // Find a non-covered zero and prime it
                if (matrix[i][j] == 0 && coveredRows[i] == 0 && coveredCols[j] == 0 && markedMatrix[i][j] == 0) {
                    markedMatrix[i][j] = 2;
                    exitLoop = false;
                    if (starInRow(i)) { //the zero is on the same row as a starred zero
                        coveredRows[i] = 1;               //cover the corresponding row
                        int colIndex = -1;
                        for (int k = 0; k < totalTasks; k++) {
                            if (markedMatrix[i][k] == 1) {
                                colIndex = k;
                                break;
                            }
                        }
                        coveredCols[colIndex] = 0;      // uncover the column of the starred zero
                    } else { //the non-covered zero has no assigned zero on its row
                        adjustPath(i, j);
                        clearCovers(coveredRows, coveredCols);
                        i = 0;
                        j = 0;
                        change = false;
                    }
                }
                if (i == totalStudents-1 && j == totalTasks-1) {
                    if (exitLoop) {
                        break;
                    } else {
                        exitLoop = true;
                    }
                }
                if (change) {
                    j++;
                    if (j == totalTasks) {
                        j = 0;
                        i++;
                        if (i == totalStudents) {
                            i = 0;
                        }
                    }
                }
            }
            adjustMatrix(coveredRows, coveredCols);
        }
        printResults();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private void reduceRows() {
        for (int i = 0; i < totalStudents; i++) {
            float min = Float.MAX_VALUE;
            for (int j = 0; j < totalTasks; j++) {
                if (matrix[i][j] < min) {
                    min = matrix[i][j];
                }
            }

            for (int j = 0; j < totalTasks; j++) {
                matrix[i][j] -= min;
            }
        }
    }

    private void reduceCols() {
        for (int j = 0; j < totalTasks; j++) {
            float min = Float.MAX_VALUE;
            for (int i = 0; i < totalStudents; i++) {
                if (matrix[i][j] < min) {
                    min = matrix[i][j];
                }
            }
            for (int i = 0; i < totalStudents; i++) {
                matrix[i][j] -= min;
            }
        }
    }

    private boolean starInRow(int row) {
        for (int j = 0; j < totalTasks; j++) {
            if (markedMatrix[row][j] == 1) {
                return true;
            }
        }
        return false;
    }

    private boolean starInCol(int col) {
        for (int i = 0; i < totalStudents; i++) {
            if (markedMatrix[i][col] == 1) {
                return true;
            }
        }
        return false;
    }

    private int starCount() {
        int count = 0;
        for (int[] rows : markedMatrix) {
            for (int elem : rows) {
                if (elem == 1) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean zeroExists(int[] coveredRows, int[] coveredCols) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0 && markedMatrix[i][j] == 0 && coveredRows[i] == 0 && coveredCols[j] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void adjustMatrix(int[] coveredRows, int[] coveredCols) {
        float min = Float.MAX_VALUE;
        for (int i = 0; i < totalStudents; i++) {
            for (int j = 0; j < totalTasks; j++) {
                if (coveredCols[j] == 0 && coveredRows[i] == 0) {
                    if (matrix[i][j] < min) {
                        min = matrix[i][j];     // find min uncovered value
                    }
                }
            }
        }

        for (int i = 0; i < totalStudents; i++) {
            for (int j = 0; j < totalTasks; j++) {
                if (coveredCols[j] == 0 && coveredRows[i] == 0) {
                    matrix[i][j] -= min;    // subtract min from every uncovered element
                } else if (coveredCols[j] == 1 && coveredRows[i] == 1) {
                    matrix[i][j] += min;    // add min to every element that is covered twice
                }
            }
        }
    }

    private void adjustPath(int i, int j) {
        int[] rowPath = new int[totalStudents * totalTasks];
        int[] colPath = new int[totalStudents * totalTasks];
        int pathSize = 0;
        int col = j;
        int row = i;

        // path starts from current primed zero
        rowPath[0] = row;
        colPath[0] = col;
        pathSize++;

        while (true) {
            boolean starFound = false;
            for (int k = 0; k < totalStudents; k++) {
                if (markedMatrix[k][col] == 1) {  // if starred zero exists in the same column with the current primed zero
                    rowPath[pathSize] = k;
                    colPath[pathSize] = col;
                    pathSize++;
                    starFound = true;
                    row = k;
                    break;
                }
            }

            if (!starFound) { break; } // if a starred zero wasn't found, break out of the loop

            boolean check = false;
            for (int k = 0; k < totalTasks; k++) {
                if (markedMatrix[row][k] == 2) {  // if a prime zero exists in the same row as the current starred zero
                    rowPath[pathSize] = row;
                    colPath[pathSize] = k;
                    col = k;
                    pathSize++;
                    check = true;
                    break;
                }
            }
            if (!check) {
                System.out.println("Error: primed not found"); // should never run if correct
                System.exit(-1);
            }
        }

        for (int k = 0; k < pathSize; k++) {  // for each element in the path
            int x = rowPath[k];
            int y = colPath[k];

            if (markedMatrix[x][y] == 1) {  // unstar all starred zeroes
                markedMatrix[x][y] = 0;
            } else if (markedMatrix[x][y] == 2) {  // star all primed zeroes
                markedMatrix[x][y] = 1;
            }
        }
    }

    private void clearCovers(int[] coveredRows, int[] coveredCols) {
        for (int i = 0; i < totalStudents; i++) {
            for (int j = 0; j < totalTasks; j++) {
                if (markedMatrix[i][j] == 2) {
                    markedMatrix[i][j] = 0;     // reset primed zeroes
                }
            }
        }
        Arrays.fill(coveredRows, 0);
        Arrays.fill(coveredCols, 0);
    }

    public void printResults() {
        float totalCost = 0;
        for (int i = 0; i < totalStudents; i++) {
            for (int j = 0; j < totalTasks; j++) {
                if (markedMatrix[i][j] == 1) {
                    totalCost += costs[i][j];
                    System.out.println("Student " + i + " -> Task " + j + " (Cost: " + costs[i][j] + ")");
                    CourseManager.getCourseByName(taskToCourse[j]).assignTa(StudentManager.students.get(i));
                    break;
                }
            }
        }
        System.out.println("Total cost: " + totalCost);
        System.out.println(Arrays.deepToString(costs));
    }
}
