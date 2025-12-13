package id.my.hendisantika.cutiaw.service;

import id.my.hendisantika.cutiaw.entity.Employee;
import id.my.hendisantika.cutiaw.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : cutiaw
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 13/12/25
 * Time: 14.55
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public boolean existsByEmployeeId(String employeeId) {
        return employeeRepository.existsByEmployeeId(employeeId);
    }

    public void updateLeaveBalance(Long employeeId, int daysUsed) {
        employeeRepository.findById(employeeId).ifPresent(employee -> {
            employee.setAnnualLeaveBalance(employee.getAnnualLeaveBalance() - daysUsed);
            employeeRepository.save(employee);
        });
    }

    public void restoreLeaveBalance(Long employeeId, int daysRestored) {
        employeeRepository.findById(employeeId).ifPresent(employee -> {
            employee.setAnnualLeaveBalance(employee.getAnnualLeaveBalance() + daysRestored);
            employeeRepository.save(employee);
        });
    }
}
