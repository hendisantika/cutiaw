package id.my.hendisantika.cutiaw.controller;

import id.my.hendisantika.cutiaw.entity.Employee;
import id.my.hendisantika.cutiaw.service.EmployeeService;
import id.my.hendisantika.cutiaw.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

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
@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final LeaveRequestService leaveRequestService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("currentPage", "employees");
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employees/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("currentPage", "employees");
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    @PostMapping
    public String createEmployee(@ModelAttribute Employee employee, RedirectAttributes redirectAttributes) {
        if (employeeService.existsByEmail(employee.getEmail())) {
            redirectAttributes.addFlashAttribute("error", "Email sudah terdaftar");
            return "redirect:/employees/new";
        }
        if (employeeService.existsByEmployeeId(employee.getEmployeeId())) {
            redirectAttributes.addFlashAttribute("error", "ID Karyawan sudah terdaftar");
            return "redirect:/employees/new";
        }

        if (employee.getJoinDate() == null) {
            employee.setJoinDate(LocalDate.now());
        }
        if (employee.getAnnualLeaveBalance() == null) {
            employee.setAnnualLeaveBalance(12);
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("success", "Karyawan berhasil ditambahkan");
        return "redirect:/employees";
    }

    @GetMapping("/{id}")
    public String viewEmployee(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "employees");
        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    model.addAttribute("employee", employee);
                    model.addAttribute("leaveRequests", leaveRequestService.getLeaveRequestsByEmployeeId(id));
                    model.addAttribute("remainingLeave", leaveRequestService.getRemainingAnnualLeave(id));
                    return "employees/view";
                })
                .orElse("redirect:/employees");
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "employees");
        return employeeService.getEmployeeById(id)
                .map(employee -> {
                    model.addAttribute("employee", employee);
                    return "employees/form";
                })
                .orElse("redirect:/employees");
    }

    @PostMapping("/{id}")
    public String updateEmployee(@PathVariable Long id, @ModelAttribute Employee employee,
                                 RedirectAttributes redirectAttributes) {
        return employeeService.getEmployeeById(id)
                .map(existingEmployee -> {
                    existingEmployee.setName(employee.getName());
                    existingEmployee.setEmail(employee.getEmail());
                    existingEmployee.setDepartment(employee.getDepartment());
                    existingEmployee.setPosition(employee.getPosition());
                    existingEmployee.setAnnualLeaveBalance(employee.getAnnualLeaveBalance());
                    employeeService.saveEmployee(existingEmployee);
                    redirectAttributes.addFlashAttribute("success", "Data karyawan berhasil diperbarui");
                    return "redirect:/employees/" + id;
                })
                .orElse("redirect:/employees");
    }

    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute("success", "Karyawan berhasil dihapus");
        return "redirect:/employees";
    }
}
