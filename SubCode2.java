import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SubCode2 {

    // Constants for maximum number of students and modules
    private static final int MAX_STUDENTS = 100;
    private static final int MAX_MODULES = 3;

    // Variable to track the current number of students
    private static int studentCount = 0;

    // Array to store student results
    private static StudentResult[] studentResults = new StudentResult[MAX_STUDENTS];

    // Method to manage student results
    public static void manageStudentResults() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        // Loop to display menu and handle user choices
        while (true) {
            displayMenu();
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addModuleMarks(scanner);
                    break;
                case 2:
                    generateStudentReport(scanner);
                    break;
                case 3:
                    storeStudentResultsToFile();
                    break;
                case 4:
                    loadStudentResultsFromFile();
                    break;
                case 5:
                    System.out.println("Exiting student results management...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to display the menu
    private static void displayMenu() {
        System.out.println("\nStudent Results Management");
        System.out.println("1. Add module marks for a student");
        System.out.println("2. Generate student report");
        System.out.println("3. Store student results into a file");
        System.out.println("4. Load student results from the file to the system");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    // Method to add module marks for a student
    private static void addModuleMarks(Scanner scanner) {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("No more students can be added.");
            return;
        }

        System.out.print("Enter student ID (e.g., w1234567): ");
        String id = scanner.nextLine().trim(); // Trim to remove leading/trailing spaces

        // Check if the entered ID exists in student details
        String studentName = SubCode1.getStudentNameById(id);
        if (studentName == null) {
            System.out.println("Student with ID " + id + " does not exist. Please register first.");
            return;
        }

        // Check if the student already has module marks recorded
        for (int i = 0; i < studentCount; i++) {
            if (studentResults[i].getStudentId().equals(id)) {
                System.out.println("Module marks already added for student with ID: " + id);
                return;
            }
        }

        // Prompt for module marks
        int[] marks = new int[MAX_MODULES];
        for (int i = 0; i < MAX_MODULES; i++) {
            System.out.print("Enter marks for Module " + (i + 1) + ": ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
            marks[i] = scanner.nextInt();
            scanner.nextLine(); // Consume newline
        }

        // Calculate average marks and determine grade
        double averageMarks = calculateAverage(marks);
        char grade = calculateGrade(averageMarks);

        // Store student results
        studentResults[studentCount++] = new StudentResult(id, studentName, marks, averageMarks, grade);
        System.out.println("Module marks added successfully.");
        storeStudentResultsToFile(); // Save results to file after adding
    }

    // Method to calculate average marks
    private static double calculateAverage(int[] marks) {
        int sum = 0;
        for (int mark : marks) {
            sum += mark;
        }
        return (double) sum / marks.length;
    }

    // Method to calculate grade based on average marks
    private static char calculateGrade(double averageMarks) {
        if (averageMarks >= 70) {
            return 'A';
        } else if (averageMarks >= 60) {
            return 'B';
        } else if (averageMarks >= 50) {
            return 'C';
        } else if (averageMarks >= 40) {
            return 'D';
        } else {
            return 'F';
        }
    }

    // Method to generate a student report
    private static void generateStudentReport(Scanner scanner) {
        System.out.print("Enter student ID to generate report: ");
        String id = scanner.nextLine();

        // Find student results by ID
        for (int i = 0; i < studentCount; i++) {
            if (studentResults[i].getStudentId().equals(id)) {
                System.out.println("Student Report:");
                System.out.println("Student ID: " + studentResults[i].getStudentId());
                System.out.println("Student Name: " + studentResults[i].getStudentName());
                System.out.println("Module Marks:");
                int[] marks = studentResults[i].getModuleMarks();
                for (int j = 0; j < MAX_MODULES; j++) {
                    System.out.println("Module " + (j + 1) + ": " + marks[j]);
                }
                System.out.println("Average Marks: " + studentResults[i].getAverageMarks());
                System.out.println("Grade: " + studentResults[i].getGrade());
                return;
            }
        }

        System.out.println("Student not found with ID: " + id);
    }

    // Method to store student results to a file
    public static void storeStudentResultsToFile() {
        try (PrintWriter writer = new PrintWriter(new File(System.getProperty("user.home") + "/Desktop/students_task2.txt"))) {
            for (int i = 0; i < studentCount; i++) {
                writer.println(studentResults[i].getStudentId() + "," + studentResults[i].getStudentName() + "," +
                        studentResults[i].getModuleMarks()[0] + "," + studentResults[i].getModuleMarks()[1] + "," +
                        studentResults[i].getModuleMarks()[2] + "," + studentResults[i].getAverageMarks() + "," +
                        studentResults[i].getGrade());
            }
            System.out.println("Student results stored to file successfully.");
        } catch (IOException e) {
            System.out.println("Error storing student results to file: " + e.getMessage());
        }
    }

    // Method to load student results from a file
    public static void loadStudentResultsFromFile() {
        File file = new File(System.getProperty("user.home") + "/Desktop/students_task2.txt");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File not found. A new file has been created: " + file.getPath());
                }
            } catch (IOException e) {
                System.out.println("Error creating new file: " + e.getMessage());
                return;
            }
        }

        try (Scanner scanner = new Scanner(file)) {
            studentCount = 0; // Reset student count before loading from file
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String id = parts[0];
                    String name = parts[1];
                    int[] marks = new int[MAX_MODULES];
                    marks[0] = Integer.parseInt(parts[2]);
                    marks[1] = Integer.parseInt(parts[3]);
                    marks[2] = Integer.parseInt(parts[4]);
                    double averageMarks = Double.parseDouble(parts[5]);
                    char grade = parts[6].charAt(0);
                    studentResults[studentCount++] = new StudentResult(id, name, marks, averageMarks, grade);
                }
            }
            System.out.println("Student results loaded from file successfully.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading student results from file: " + e.getMessage());
        }
    }

    // Inner class to represent student results
    private static class StudentResult {
        private String studentId;
        private String studentName;
        private int[] moduleMarks;
        private double averageMarks;
        private char grade;

        public StudentResult(String studentId, String studentName, int[] moduleMarks, double averageMarks, char grade) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.moduleMarks = moduleMarks;
            this.averageMarks = averageMarks;
            this.grade = grade;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public int[] getModuleMarks() {
            return moduleMarks;
        }

        public double getAverageMarks() {
            return averageMarks;
        }

        public char getGrade() {
            return grade;
        }
    }
}
