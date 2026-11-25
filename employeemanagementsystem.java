import java.util.*;
import java.io.*;

class Employee {
    String id, name, department;
    double salary;

    Employee(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
}

class Node {
    Employee data;
    Node next;

    Node(Employee data) {
        this.data = data;
        this.next = null;
    }
}

class EmployeeManagementSystem {

    Node head = null;
    HashMap<String, Employee> employeeMap = new HashMap<>();
    int idCount = 101;

    // Add Employee
    void addEmployee(String name, String department, double salary) {

        if (salary <= 0) {
            System.out.println("Salary must be greater than zero.");
            return;
        }

        String id = "E" + idCount++;
        Employee emp = new Employee(id, name, department, salary);

        Node newNode = new Node(emp);

        if (head == null) {
            head = newNode;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }

        employeeMap.put(id, emp);

        System.out.println("Employee added. ID: " + id +
                " | " + name + " | " + department + " | " + salary);
    }

    // Display Employees
    void displayEmployees() {

        if (head == null) {
            System.out.println("No employees found.");
            return;
        }

        Node temp = head;

        while (temp != null) {
            Employee e = temp.data;

            System.out.println(e.id + " | " + e.name +
                    " | " + e.department + " | " + e.salary);

            temp = temp.next;
        }
    }

    // Search by ID
    void searchById(String id) {

        Employee e = employeeMap.get(id);

        if (e != null) {
            System.out.println(e.id + " | " + e.name +
                    " | " + e.department + " | " + e.salary);
        } else {
            System.out.println("Employee " + id + " not found.");
        }
    }

    // Search by Name
    void searchByName(String name) {

        Node temp = head;
        boolean found = false;

        while (temp != null) {
            Employee e = temp.data;

            if (e.name.equalsIgnoreCase(name)) {
                System.out.println(e.id + " | " + e.name +
                        " | " + e.department + " | " + e.salary);
                found = true;
            }

            temp = temp.next;
        }

        if (!found) {
            System.out.println("Employee not found.");
        }
    }

    // Delete Employee
    void deleteEmployee(String id) {

        if (head == null) {
            System.out.println("No employees available.");
            return;
        }

        if (head.data.id.equalsIgnoreCase(id)) {
            head = head.next;
            employeeMap.remove(id);
            System.out.println("Employee " + id + " deleted.");
            System.out.println("Total employees: " + countEmployees());
            return;
        }

        Node temp = head;

        while (temp.next != null) {

            if (temp.next.data.id.equalsIgnoreCase(id)) {
                temp.next = temp.next.next;
                employeeMap.remove(id);

                System.out.println("Employee " + id + " deleted.");
                System.out.println("Total employees: " + countEmployees());
                return;
            }

            temp = temp.next;
        }

        System.out.println("Employee " + id + " not found.");
    }

    // Count Employees
    int countEmployees() {
        int count = 0;
        Node temp = head;

        while (temp != null) {
            count++;
            temp = temp.next;
        }

        return count;
    }

    // Sort Salary High to Low
    void sortBySalary() {

        if (head == null || head.next == null) {
            displayEmployees();
            return;
        }

        for (Node i = head; i != null; i = i.next) {
            for (Node j = i.next; j != null; j = j.next) {

                if (i.data.salary < j.data.salary) {
                    Employee temp = i.data;
                    i.data = j.data;
                    j.data = temp;
                }
            }
        }

        System.out.println("Employees sorted by salary (High to Low):");
        displayEmployees();
    }

    // Save Data to File
    void saveToFile() {

        try (PrintWriter writer =
                new PrintWriter(new FileWriter("employees.txt"))) {

            Node temp = head;

            while (temp != null) {
                Employee e = temp.data;

                writer.println(e.id + "," + e.name + "," +
                        e.department + "," + e.salary);

                temp = temp.next;
            }

        } catch (IOException e) {
            System.out.println("Error saving file.");
        }
    }

    // Load Data from File
    void loadFromFile() {

        File file = new File("employees.txt");

        if (!file.exists()) return;

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                String[] data = line.split(",", 4);

                if (data.length != 4) continue;

                try {
                    String id = data[0];
                    String name = data[1];
                    String department = data[2];
                    double salary = Double.parseDouble(data[3]);

                    if (salary <= 0 || employeeMap.containsKey(id))
                        continue;

                    Employee emp = new Employee(
                            id, name, department, salary);

                    Node newNode = new Node(emp);

                    if (head == null) {
                        head = newNode;
                    } else {
                        Node temp = head;
                        while (temp.next != null)
                            temp = temp.next;
                        temp.next = newNode;
                    }

                    employeeMap.put(id, emp);

                    int number = Integer.parseInt(id.substring(1));
                    if (number >= idCount)
                        idCount = number + 1;

                } catch (NumberFormatException e) {
                    System.out.println("Invalid employee record skipped.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading file.");
        }
    }

    // Main Method
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EmployeeManagementSystem system =
                new EmployeeManagementSystem();

        system.loadFromFile();

        int choice;

        do {
            System.out.println("\n===== EMPLOYEE MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Employee");
            System.out.println("2. Display Employees");
            System.out.println("3. Search Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Sort by Salary (High to Low)");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");

            try {
                choice = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number.");
                sc.nextLine();
                continue;
            }

            switch (choice) {

                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter Department: ");
                    String department = sc.nextLine();

                    System.out.print("Enter Salary: ");

                    try {
                        double salary = sc.nextDouble();
                        sc.nextLine();

                        system.addEmployee(name, department, salary);
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid salary.");
                        sc.nextLine();
                    }
                    break;

                case 2:
                    system.displayEmployees();
                    break;

                case 3:
                    System.out.println("1. Search by ID");
                    System.out.println("2. Search by Name");

                    int searchChoice = sc.nextInt();
                    sc.nextLine();

                    if (searchChoice == 1) {
                        System.out.print("Enter Employee ID: ");
                        system.searchById(sc.nextLine());
                    } else if (searchChoice == 2) {
                        System.out.print("Enter Employee Name: ");
                        system.searchByName(sc.nextLine());
                    } else {
                        System.out.println("Invalid choice.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Employee ID to delete: ");
                    String id = sc.nextLine();

                    system.deleteEmployee(id);
                    break;

                case 5:
                    system.sortBySalary();
                    break;

                case 6:
                    system.saveToFile();
                    System.out.println("Data saved successfully.");
                    System.out.println("Exiting Employee Management System.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (true);

    }
}
