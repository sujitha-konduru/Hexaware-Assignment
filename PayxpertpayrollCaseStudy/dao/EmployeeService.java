package dao;

import Entity.Employee;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService implements IEmployeeService {

    private final String url = "jdbc:mysql://localhost:3306/unisoft";
    private final String username = "root";
    private final String password = "2215";

    @Override
    public void addEmployee(Employee emp) {
        String query = "INSERT INTO employee (EmployeeID, FirstName, LastName, DateOfBirth, Gender, Email, PhoneNumber, Address, Position, JoiningDate, TerminationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, emp.getEmployeeID());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getLastName());
            ps.setDate(4, Date.valueOf(emp.getDateOfBirth()));
            ps.setString(5, emp.getGender());
            ps.setString(6, emp.getEmail());
            ps.setString(7, emp.getPhoneNumber());
            ps.setString(8, emp.getAddress());
            ps.setString(9, emp.getPosition());
            ps.setDate(10, Date.valueOf(emp.getJoiningDate()));
            if (emp.getTerminationDate() != null) {
                ps.setDate(11, Date.valueOf(emp.getTerminationDate()));
            } else {
                ps.setNull(11, java.sql.Types.DATE);
            }

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int employeeId = generatedKeys.getInt(1);
                        System.out.println("Employee added to the database. ID: " + employeeId);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error while inserting employee:");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw new RuntimeException("Failed to insert employee: " + e.getMessage(), e);

        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("EmployeeID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getDate("DateOfBirth").toLocalDate(),
                    rs.getString("Gender"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Address"),
                    rs.getString("Position"),
                    rs.getDate("JoiningDate").toLocalDate(),
                    rs.getDate("TerminationDate") != null ? rs.getDate("TerminationDate").toLocalDate() : null
                );
                employees.add(emp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public void updateEmployee(Employee emp) {
        String query = "UPDATE employee SET FirstName=?, LastName=?, DateOfBirth=?, Gender=?, Email=?, PhoneNumber=?, Address=?, Position=?, JoiningDate=?, TerminationDate=? WHERE EmployeeID=?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, emp.getFirstName());
            ps.setString(2, emp.getLastName());
            ps.setDate(3, Date.valueOf(emp.getDateOfBirth()));
            ps.setString(4, emp.getGender());
            ps.setString(5, emp.getEmail());
            ps.setString(6, emp.getPhoneNumber());
            ps.setString(7, emp.getAddress());
            ps.setString(8, emp.getPosition());
            ps.setDate(9, Date.valueOf(emp.getJoiningDate()));
            if (emp.getTerminationDate() != null) {
                ps.setDate(10, Date.valueOf(emp.getTerminationDate()));
            } else {
                ps.setNull(10, java.sql.Types.DATE);
            }
            ps.setInt(11, emp.getEmployeeID());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println(rows + " employee(s) updated.");
            } else {
                System.out.println("No employee found with ID: " + emp.getEmployeeID());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(int employeeId) {
        String query = "DELETE FROM employee WHERE EmployeeID=?";
        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No employee found with ID: " + employeeId);
            }

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeById(int id) {
        String query = "SELECT * FROM employee WHERE EmployeeID=?";
        Employee emp = null;

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                emp = new Employee(
                    rs.getInt("EmployeeID"),
                    rs.getString("FirstName"),
                    rs.getString("LastName"),
                    rs.getDate("DateOfBirth").toLocalDate(),
                    rs.getString("Gender"),
                    rs.getString("Email"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Address"),
                    rs.getString("Position"),
                    rs.getDate("JoiningDate").toLocalDate(),
                    rs.getDate("TerminationDate") != null ? rs.getDate("TerminationDate").toLocalDate() : null
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emp;
    }
}
