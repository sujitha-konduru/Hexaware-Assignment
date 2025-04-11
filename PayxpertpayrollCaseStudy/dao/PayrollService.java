package dao;

import Entity.Payroll;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PayrollService implements IPayrollService {
    private final String url = "jdbc:mysql://localhost:3306/unisoft";
    private final String username = "root";
    private final String password = "2215";

    @Override
    public Payroll generatePayroll(int employeeId, LocalDate startDate, LocalDate endDate,
                                   double basicSalary, double overtimePay, double deductions) {
        double netSalary = basicSalary + overtimePay - deductions;
        String query = "INSERT INTO Payroll (EmployeeID, PayPeriodStartDate, PayPeriodEndDate, BasicSalary, OvertimePay, Deductions, NetSalary) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, employeeId);
            ps.setDate(2, Date.valueOf(startDate));
            ps.setDate(3, Date.valueOf(endDate));
            ps.setDouble(4, basicSalary);
            ps.setDouble(5, overtimePay);
            ps.setDouble(6, deductions);
            ps.setDouble(7, netSalary);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int payrollId = rs.getInt(1);
                    return new Payroll(payrollId, employeeId, startDate, endDate, basicSalary, overtimePay, deductions, netSalary);
                }
            }
        } catch (SQLException e) {
        	System.err.println("Error generating payroll: " + e.getMessage());
        // Throw exception so assertThrows() can work
        throw new RuntimeException("Error generating payroll: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Payroll> getAllPayrolls() {
        List<Payroll> payrolls = new ArrayList<>();
        String query = "SELECT * FROM Payroll";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Payroll p = new Payroll(
                    rs.getInt("PayrollID"),
                    rs.getInt("EmployeeID"),
                    rs.getDate("PayPeriodStartDate").toLocalDate(),
                    rs.getDate("PayPeriodEndDate").toLocalDate(),
                    rs.getDouble("BasicSalary"),
                    rs.getDouble("OvertimePay"),
                    rs.getDouble("Deductions"),
                    rs.getDouble("NetSalary")
                );
                payrolls.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payrolls;
    }

    @Override
    public List<Payroll> getPayrollsForEmployee(int employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String query = "SELECT * FROM Payroll WHERE EmployeeID = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Payroll p = new Payroll(
                    rs.getInt("PayrollID"),
                    rs.getInt("EmployeeID"),
                    rs.getDate("PayPeriodStartDate").toLocalDate(),
                    rs.getDate("PayPeriodEndDate").toLocalDate(),
                    rs.getDouble("BasicSalary"),
                    rs.getDouble("OvertimePay"),
                    rs.getDouble("Deductions"),
                    rs.getDouble("NetSalary")
                );
                payrolls.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return payrolls;
    }
    public void deletePayrollsForEmployeeYear(int employeeId, int year) {
        String query = "DELETE FROM Payroll WHERE EmployeeID = ? AND YEAR(PayPeriodEndDate) = ?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ps.setInt(2, year);
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error deleting payrolls: " + e.getMessage());
        }
    }


}
