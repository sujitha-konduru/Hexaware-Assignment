package dao;

import Entity.Employee;
import java.util.List;

public interface IEmployeeService {
    void addEmployee(Employee emp);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(int id);
    void updateEmployee(Employee emp);
    void deleteEmployee(int employeeId);
}
