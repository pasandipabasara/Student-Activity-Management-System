import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SubCode1 {

    // Constants for maximum students
    private static final int MAX_STUDENTS = 100;

    // Variables to keep track of student count and store student details
    private static int studentCount = 0;
    private static Student[] students = new Student[MAX_STUDENTS];

    // Method to manage student operations
    public static void manageStudents() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            displayMenu(); // Display the menu options
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Handle the user's choice
            switch (choice) {
                case 1:
                    checkAvailableSeats();
                    break;
                case 2:
                    registerStudent(scanner);
                    break;
                case 3:
                    deleteStudent(scanner);
                    break;
                case 4:
                    findStudent(scanner);
                    break;
                case 5:
                    storeStudentDetailsToFile();
                    break;
                case 6:
                    loadStudentDetailsFromFile();
                    break;
                case 7:
                    viewStudentsSortedByName();
                    break;
                case 8:
                    System.out.println("Exiting student management...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to display the menu options
    private static void displayMenu() {
        System.out.println("\nUniversity Intake Management System");
        System.out.println("1. Check available seats");
        System.out.println("2. Register student ");
        System.out.println("3. Delete student");
        System.out.println("4. Find student (with student ID)");
        System.out.println("5. Store student details into a file");
        System.out.println("6. Load student details from the file to the system");
        System.out.println("7. View the list of students based on their names");
        System.out.println("8. Exit");
        System.out.print("Enter your choice: ");
    }

    // Method to check available seats
    private static void checkAvailableSeats() {
        int availableSeats = MAX_STUDENTS - studentCount;
        System.out.println("Available seats: " + availableSeats);
    }

    // Method to register a new student
    private static void registerStudent(Scanner scanner) {
        if (studentCount >= MAX_STUDENTS) {
            System.out.println("No available seats. Registration is full.");
            return;
        }

        System.out.print("Enter student ID (e.g., w1234567): ");
        String id = scanner.nextLine().trim(); // Trim to remove leading/trailing spaces

        // Check if the entered ID already exists
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                System.out.println("Student with ID " + id + " already exists. Registration failed.");
                return;
            }
        }

        // Validate the student ID format
        if (!id.matches("w\\d{7}")) {
            System.out.println("Invalid student ID format. Please enter a valid ID.");
            return;
        }

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        students[studentCount++] = new Student(id, name); // Store student in array and increment count
        System.out.println("Student registered successfully.");
        storeStudentDetailsToFile(); // Save details to file after registration
    }

    // Method to delete a student
    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        String id = scanner.nextLine();
        boolean found = false;

        // Find and remove the student from the array
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                // Remove student from array and adjust count
                for (int j = i; j < studentCount - 1; j++) {
                    students[j] = students[j + 1];
                }
                students[--studentCount] = null;
                System.out.println("Student deleted successfully.");
                found = true;
                storeStudentDetailsToFile(); // Save details to file after deletion
                break;
            }
        }
        if (!found) {
            System.out.println("Student not found with ID: " + id);
        }
    }

    // Method to find a student by ID
    private static void findStudent(Scanner scanner) {
        System.out.print("Enter student ID to find: ");
        String id = scanner.nextLine();

        // Search for the student in the array
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(id)) {
                System.out.println("Student found:");
                System.out.println("ID: " + students[i].getId());
                System.out.println("Name: " + students[i].getName());
                return;
            }
        }
        System.out.println("Student not found with ID: " + id);
    }

    // Method to view students sorted by name
    private static void viewStudentsSortedByName() {
        // Create a temporary array to hold active students
        Student[] activeStudents = new Student[studentCount];
        int activeCount = 0;

        // Copy active students to the temporary array
        for (int i = 0; i < studentCount; i++) {
            if (students[i] != null) {
                activeStudents[activeCount++] = students[i];
            }
        }

        // Sort active students by name (bubble sort)
        for (int i = 0; i < activeCount - 1; i++) {
            for (int j = 0; j < activeCount - i - 1; j++) {
                if (activeStudents[j].getName().compareToIgnoreCase(activeStudents[j + 1].getName()) > 0) {
                    // Swap
                    Student temp = activeStudents[j];
                    activeStudents[j] = activeStudents[j + 1];
                    activeStudents[j + 1] = temp;
                }
            }
        }

        // Display sorted active students
        System.out.println("Sorted List of Students:");
        for (int i = 0; i < activeCount; i++) {
            System.out.println("ID: " + activeStudents[i].getId() + ", Name: " + activeStudents[i].getName());
        }
    }

    // Method to store student details to a file
    public static void storeStudentDetailsToFile() {
        try (PrintWriter writer = new PrintWriter(new File(System.getProperty("user.home") + "/Desktop/students_task1.txt"))) {
            for (int i = 0; i < studentCount; i++) {
                writer.println(students[i].getId() + "," + students[i].getName());
            }
            System.out.println("Student details stored to file successfully.");
        } catch (IOException e) {
            System.out.println("Error storing student details to file: " + e.getMessage());
        }
    }

    // Method to load student details from a file
    public static void loadStudentDetailsFromFile() {
        File file = new File(System.getProperty("user.home") + "/Desktop/students_task1.txt");
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
                if (parts.length == 2) {
                    students[studentCount++] = new Student(parts[0], parts[1]);
                }
            }
            System.out.println("Student details loaded from file successfully.");
        } catch (IOException e) {
            System.out.println("Error loading student details from file: " + e.getMessage());
        }
    }

    // Method to get student name by ID
    public static String getStudentNameById(String studentId) {
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId().equals(studentId)) {
                return students[i].getName();
            }
        }
        return null; // Return null if student ID is not found
    }

    // Inner class to represent a student
    private static class Student {
        private String id;
        private String name;

        public Student(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
