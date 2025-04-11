package dao;

import Entity.FinancialRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialRecordService implements IFinancialRecordService {

    private final String url = "jdbc:mysql://localhost:3306/unisoft";
    private final String username = "root";
    private final String password = "2215";

    @Override
    public void addFinancialRecord(FinancialRecord record) {
        String query = "INSERT INTO financialrecord (EmployeeID, RecordDate, Description, Amount, RecordType) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, record.getEmployeeID());
            ps.setDate(2, Date.valueOf(record.getRecordDate()));
            ps.setString(3, record.getDescription());
            ps.setDouble(4, record.getAmount());
            ps.setString(5, record.getRecordType());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId) {
        List<FinancialRecord> records = new ArrayList<>();
        String query = "SELECT * FROM financialrecord WHERE EmployeeID = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, employeeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                FinancialRecord record = new FinancialRecord(
                        rs.getInt("RecordID"),
                        rs.getInt("EmployeeID"),
                        rs.getDate("RecordDate").toLocalDate(),
                        rs.getString("Description"),
                        rs.getDouble("Amount"),
                        rs.getString("RecordType")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public double getTotalAmountByType(String type) {
        String query = "SELECT SUM(Amount) as total FROM financialrecord WHERE RecordType = ?";
        double total = 0.0;

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, type.toLowerCase());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }
}
