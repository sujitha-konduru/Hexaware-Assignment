package dao;

import Entity.Tax;
import exception.DatabaseConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaxService implements ITaxService {
    private Connection getConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/unisoft", "root", "2215");
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Database connection failed.");
        }
    }

    @Override
    public double calculateTax(int employeeId, int taxYear) {
        double taxableIncome = 0;
        double taxAmount = 0;

        try (Connection conn = getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT SUM(NetSalary) FROM Payroll WHERE EmployeeID = ? AND YEAR(PayPeriodEndDate) = ?");
            stmt.setInt(1, employeeId);
            stmt.setInt(2, taxYear);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                taxableIncome = rs.getDouble(1);
                taxAmount = taxableIncome * 0.15;
            }

            PreparedStatement insert = conn.prepareStatement("INSERT INTO Tax (EmployeeID, TaxYear, TaxableIncome, TaxAmount) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE TaxableIncome=?, TaxAmount=?");
            insert.setInt(1, employeeId);
            insert.setInt(2, taxYear);
            insert.setDouble(3, taxableIncome);
            insert.setDouble(4, taxAmount);
            insert.setDouble(5, taxableIncome);
            insert.setDouble(6, taxAmount);
            insert.executeUpdate();

        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }

        return taxAmount;
    }

    @Override
    public List<Tax> getAllTaxes() {
        List<Tax> taxes = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Tax");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tax tax = new Tax();
                tax.setTaxID(rs.getInt("TaxID"));
                tax.setEmployeeID(rs.getInt("EmployeeID"));
                tax.setTaxYear(rs.getInt("TaxYear"));
                tax.setTaxableIncome(rs.getDouble("TaxableIncome"));
                tax.setTaxAmount(rs.getDouble("TaxAmount"));
                taxes.add(tax);
            }
        } catch (SQLException | DatabaseConnectionException e) {
            e.printStackTrace();
        }

        return taxes;
    }
}
