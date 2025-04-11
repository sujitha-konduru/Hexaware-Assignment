package dao;

import Entity.FinancialRecord;
import java.util.List;

public interface IFinancialRecordService {
    void addFinancialRecord(FinancialRecord record);
    List<FinancialRecord> getFinancialRecordsForEmployee(int employeeId);
    double getTotalAmountByType(String type); // "income" or "expense"
}
