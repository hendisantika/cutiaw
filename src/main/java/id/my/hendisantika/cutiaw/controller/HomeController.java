package id.my.hendisantika.cutiaw.controller;

import id.my.hendisantika.cutiaw.entity.LeaveStatus;
import id.my.hendisantika.cutiaw.service.EmployeeService;
import id.my.hendisantika.cutiaw.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
@RequiredArgsConstructor
public class HomeController {

    private final EmployeeService employeeService;
    private final LeaveRequestService leaveRequestService;

    @GetMapping("/")
    public String home(Model model) {
        var allRequests = leaveRequestService.getAllLeaveRequests();
        var pendingCount = allRequests.stream()
                .filter(r -> r.getStatus() == LeaveStatus.PENDING)
                .count();
        var approvedCount = allRequests.stream()
                .filter(r -> r.getStatus() == LeaveStatus.APPROVED)
                .count();

        model.addAttribute("currentPage", "home");
        model.addAttribute("totalEmployees", employeeService.getAllEmployees().size());
        model.addAttribute("totalRequests", allRequests.size());
        model.addAttribute("pendingRequests", pendingCount);
        model.addAttribute("approvedRequests", approvedCount);
        model.addAttribute("recentRequests", allRequests.stream().limit(5).toList());

        return "index";
    }
}
