package testing;

import dao.EmployeeService;
import dao.PayrollService;
import dao.TaxService;
import Entity.Employee;
import Entity.Payroll;
import Entity.Tax;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeManagementTesting {

    private EmployeeService employeeService;
    private PayrollService payrollService;
    private TaxService taxService;
    private Employee sharedEmployee;

    @BeforeAll
    public void setup() {
        employeeService = new EmployeeService();
        payrollService = new PayrollService();
        taxService = new TaxService();

        sharedEmployee = new Employee(
                0,
                "kushal",
                "konduru",
                LocalDate.of(1990, 1, 1),
                "male",
                "kushal@email.com",
                "7777777777",
                "123 Main St",
                "Developer",
                LocalDate.of(2022, 1, 1),
                null
        );

        // Add employee to DB
        employeeService.addEmployee(sharedEmployee);

        // Fetch employee with correct phone number
        sharedEmployee = employeeService.getAllEmployees().stream()
                .filter(e -> e.getPhoneNumber().equals("5555555555"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shared employee not found after insert"));

        System.out.println("Employee added to the database. ID: " + sharedEmployee.getEmployeeID());
    }

    @Test
    public void testCalculateGrossSalaryForEmployee() {
        Payroll payroll = payrollService.generatePayroll(sharedEmployee.getEmployeeID(),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 31),
                60000,
                5000,
                2000);

        assertNotNull(payroll, "Payroll should not be null");

        // DB validation
        List<Payroll> payrolls = payrollService.getPayrollsForEmployee(sharedEmployee.getEmployeeID());
        assertFalse(payrolls.isEmpty(), "Payroll should be stored in DB");

        double expectedGross = 60000 + 5000;
        double actualGross = payroll.getBasicSalary() + payroll.getOvertimePay();
        assertEquals(expectedGross, actualGross, 0.01, "Gross salary mismatch");
    }

    @Test
    public void testCalculateNetSalaryAfterDeductions() {
        Payroll payroll = payrollService.generatePayroll(sharedEmployee.getEmployeeID(),
                LocalDate.of(2023, 2, 1),
                LocalDate.of(2023, 2, 28),
                50000,
                2000,
                3000);

        assertNotNull(payroll, "Payroll should not be null");

        double expectedNet = (50000 + 2000) - 3000;
        assertEquals(expectedNet, payroll.getNetSalary(), 0.01, "Net salary mismatch");
    }

    @Test
    public void testVerifyTaxCalculationForHighIncomeEmployee() {
        int year = 2024;

        // Clean old records
        payrollService.deletePayrollsForEmployeeYear(sharedEmployee.getEmployeeID(), year);

        payrollService.generatePayroll(sharedEmployee.getEmployeeID(),
                LocalDate.of(year, 3, 1),
                LocalDate.of(year, 3, 31),
                150000,
                10000,
                5000);

        double expectedTax = 0.15 * (150000 + 10000 - 5000);
        double actualTax = taxService.calculateTax(sharedEmployee.getEmployeeID(), year);

        System.out.println("Expected Tax: " + expectedTax);
        System.out.println("Actual Tax: " + actualTax);

        assertEquals(expectedTax, actualTax, 0.01, "Tax calculation mismatch");

        // Confirm Tax entry is saved
        List<Tax> taxes = taxService.getAllTaxes();
        boolean taxFound = taxes.stream()
                .anyMatch(t -> t.getEmployeeID() == sharedEmployee.getEmployeeID() && t.getTaxYear() == year);
        assertTrue(taxFound, "Tax record should be stored in DB");
    }

    @Test
    public void testProcessPayrollForMultipleEmployees() {
        Payroll payroll1 = payrollService.generatePayroll(sharedEmployee.getEmployeeID(),
                LocalDate.of(2023, 4, 1),
                LocalDate.of(2023, 4, 30),
                45000,
                3000,
                1000);

        Payroll payroll2 = payrollService.generatePayroll(sharedEmployee.getEmployeeID(),
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 5, 31),
                70000,
                4000,
                2000);

        assertNotNull(payroll1, "Payroll 1 should not be null");
        assertNotNull(payroll2, "Payroll 2 should not be null");

        List<Payroll> payrolls = payrollService.getPayrollsForEmployee(sharedEmployee.getEmployeeID());
        assertTrue(payrolls.size() >= 2, "Payrolls for employee should be recorded in DB");
    }

    @AfterAll
    public void tearDown() {
        // Optional cleanup
        // employeeService.deleteEmployee(sharedEmployee.getEmployeeID());
        System.out.println("All tests completed.");
    }
}
