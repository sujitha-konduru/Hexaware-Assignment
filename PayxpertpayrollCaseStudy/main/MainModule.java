package main;

import Entity.Employee;
import Entity.Payroll;
import Entity.Tax;
import Entity.FinancialRecord;
import exception.EmployeeNotFoundException;
import exception.PayrollGenerationException;
import exception.TaxCalculationException;
import exception.FinancialRecordException;
import exception.InvalidInputException;
import exception.DatabaseConnectionException;

import dao.IEmployeeService;
import dao.EmployeeService;
import dao.IPayrollService;
import dao.PayrollService;
import dao.ITaxService;
import dao.TaxService;
import dao.IFinancialRecordService;
import dao.FinancialRecordService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    public static void main(String[] args) throws EmployeeNotFoundException, PayrollGenerationException, TaxCalculationException, FinancialRecordException {
        Scanner scanner = new Scanner(System.in);

        IEmployeeService employeeService = new EmployeeService();
        IPayrollService payrollService = new PayrollService();
        ITaxService taxService = new TaxService();
        IFinancialRecordService financialService = new FinancialRecordService();

        boolean exit = false;

        System.out.println("=== Employee Management System ===");

        while (!exit) {
        	System.out.println("\nMENU:");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Generate Payroll");
            System.out.println("6. View All Payrolls");
            System.out.println("7. View Payrolls for an Employee");
            System.out.println("8. Calculate Tax");
            System.out.println("9. Add Financial Record");
            System.out.println("10. Generate Financial Report");
            System.out.println("11. Exit");
            System.out.print("Choose an option: ");


            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try {
                switch (choice) {
                    case 1:
                        System.out.print("First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Date of Birth (YYYY-MM-DD): ");
                        LocalDate dob = LocalDate.parse(scanner.nextLine());
                        System.out.print("Gender: ");
                        String gender = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Phone Number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Position: ");
                        String position = scanner.nextLine();
                        System.out.print("Joining Date (YYYY-MM-DD): ");
                        LocalDate joiningDate = LocalDate.parse(scanner.nextLine());
                        System.out.print("Termination Date (YYYY-MM-DD) or press Enter if not applicable: ");
                        String terminationInput = scanner.nextLine();
                        LocalDate terminationDate = terminationInput.isEmpty() ? null : LocalDate.parse(terminationInput);

                        Employee newEmp = new Employee(0, firstName, lastName, dob, gender, email, phone, address, position, joiningDate, terminationDate);
                        employeeService.addEmployee(newEmp);

                        System.out.println("Employee added successfully.");
                        break;

                    case 2:
                        List<Employee> employees = employeeService.getAllEmployees();
                        System.out.println("\nAll Employees:");
                        for (Employee emp : employees) {
                            System.out.println(emp.getEmployeeID() + " - " + emp.getFirstName() + " " + emp.getLastName());
                        }
                        break;
                    case 3:
                        System.out.print("Enter Employee ID to update: ");
                        int updateId = scanner.nextInt();
                        scanner.nextLine();
                        Employee empToUpdate = employeeService.getEmployeeById(updateId);
                        if (empToUpdate != null) {
                            System.out.print("New Email: ");
                            empToUpdate.setEmail(scanner.nextLine());
                            System.out.print("New Phone Number: ");
                            empToUpdate.setPhoneNumber(scanner.nextLine());
                            System.out.print("New Address: ");
                            empToUpdate.setAddress(scanner.nextLine());
                            employeeService.updateEmployee(empToUpdate);
                            System.out.println("Employee updated.");
                        } else {
                            System.out.println("Employee not found.");
                        }
                        break;

                    case 4:
                        System.out.print("Enter Employee ID to delete: ");
                        int deleteId = scanner.nextInt();
                        employeeService.deleteEmployee(deleteId);
                        System.out.println("Employee deleted.");
                        break;

                    case 5:
                        System.out.print("Employee ID: ");
                        int empId = scanner.nextInt();
                        scanner.nextLine();

                        System.out.print("Pay Start Date (YYYY-MM-DD): ");
                        LocalDate start = LocalDate.parse(scanner.nextLine());

                        System.out.print("Pay End Date (YYYY-MM-DD): ");
                        LocalDate end = LocalDate.parse(scanner.nextLine());

                        System.out.print("Basic Salary: ");
                        double baseSalary = scanner.nextDouble();

                        System.out.print("Overtime Pay: ");
                        double overtimePay = scanner.nextDouble();

                        System.out.print("Deductions: ");
                        double deductions = scanner.nextDouble();
                        scanner.nextLine(); // Clear buffer

                        Payroll payroll = payrollService.generatePayroll(empId, start, end, baseSalary, overtimePay, deductions);

                        if (payroll != null) {
                            System.out.println("✅ Payroll Generated!");
                            System.out.println("Net Salary: ₹" + payroll.getNetSalary());
                        } else {
                            System.out.println("❌ Failed to generate payroll.");
                        }
                        break;

                    case 6:
                        List<Payroll> allPayrolls = payrollService.getAllPayrolls();
                        System.out.println("\nAll Payroll Records:");
                        for (Payroll p : allPayrolls) {
                            System.out.println(p);
                        }
                        break;

                    case 7:
                        System.out.print("Enter Employee ID to view payrolls: ");
                        int searchEmpId = scanner.nextInt();
                        scanner.nextLine();

                        List<Payroll> empPayrolls = payrollService.getPayrollsForEmployee(searchEmpId);
                        if (empPayrolls.isEmpty()) {
                            System.out.println("No payrolls found for employee ID " + searchEmpId);
                        } else {
                            for (Payroll p : empPayrolls) {
                                System.out.println(p);
                            }
                        }
                        break;

                    case 8:
                        System.out.print("Employee ID: ");
                        int taxEmpId = scanner.nextInt();
                        System.out.print("Tax Year: ");
                        int taxYear = scanner.nextInt();
                        double taxAmount = taxService.calculateTax(taxEmpId, taxYear);
                        System.out.println("Tax Calculated: ₹" + taxAmount);
                        break;

                    case 9:
                        System.out.print("Employee ID: ");
                        int frEmpId = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Description: ");
                        String desc = scanner.nextLine();
                        System.out.print("Amount: ");
                        double amt = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Record Type (income/expense): ");
                        String type = scanner.nextLine();
                        FinancialRecord record = new FinancialRecord(0, frEmpId, LocalDate.now(), desc, amt, type);
                        financialService.addFinancialRecord(record);
                        System.out.println("Financial Record Added.");
                        break;
                    case 10:

                        System.out.println("\n Financial Report:");
                        double totalIncome = financialService.getTotalAmountByType("income");
                        double totalExpense = financialService.getTotalAmountByType("expense");
                        double netProfit = totalIncome - totalExpense;

                        System.out.println("Total Income: ₹" + totalIncome);
                        System.out.println("Total Expense: ₹" + totalExpense);
                        System.out.println("Net Profit: ₹" + netProfit);

                        System.out.println("\n Tax Summary Per Employee:");
                        List<Tax> taxList = taxService.getAllTaxes();
                        for (Tax t : taxList) {
                            System.out.println("Employee ID: " + t.getEmployeeID() +
                                    " | Year: " + t.getTaxYear() +
                                    " | Taxable Income: ₹" + t.getTaxableIncome() +
                                    " | Tax Amount: ₹" + t.getTaxAmount());
                        }
                        break;

                    case 11:
                        exit = true;
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (InvalidInputException | DatabaseConnectionException e) {
                System.err.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Unexpected error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
