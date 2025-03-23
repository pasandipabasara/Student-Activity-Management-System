import java.util.Scanner;

public class MainProgram {
    public static void main(String[] args) {
        // Load student details and results from files
        SubCode1.loadStudentDetailsFromFile();
        SubCode2.loadStudentResultsFromFile();

        // Main loop
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            displayMainMenu(); // Display the main menu options

            // Ensure valid integer input for choice
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume invalid input
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            // Handle the user's choice
            switch (choice) {
                case 1:
                    // Call the method to manage student details
                    SubCode1.manageStudents();
                    break;
                case 2:
                    // Call the method to manage student results and reports
                    SubCode2.manageStudentResults();
                    break;
                case 3:
                    // Exit the program
                    System.out.println("Exiting the program. Goodbye!");
                    return;
                default:
                    // Handle invalid menu choice
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to display the main menu options
    private static void displayMainMenu() {
        System.out.println("\nMain Program");
        System.out.println("1. Manage student details");
        System.out.println("2. Manage student results and reports");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }
}
