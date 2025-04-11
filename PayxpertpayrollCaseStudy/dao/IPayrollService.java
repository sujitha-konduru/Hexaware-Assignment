package dao;

import Entity.Payroll;
import java.time.LocalDate;
import java.util.List;

public interface IPayrollService {
	Payroll generatePayroll(int employeeId, LocalDate startDate, LocalDate endDate, double basicSalary, double overtimePay, double deductions);
	List<Payroll> getAllPayrolls();
	List<Payroll> getPayrollsForEmployee(int employeeId);

}
