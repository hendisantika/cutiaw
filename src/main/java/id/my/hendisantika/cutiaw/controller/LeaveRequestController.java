package id.my.hendisantika.cutiaw.controller;

import id.my.hendisantika.cutiaw.entity.LeaveRequest;
import id.my.hendisantika.cutiaw.entity.LeaveType;
import id.my.hendisantika.cutiaw.service.EmployeeService;
import id.my.hendisantika.cutiaw.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
@RequestMapping("/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final EmployeeService employeeService;

    @GetMapping
    public String listLeaveRequests(Model model) {
        model.addAttribute("currentPage", "leave-requests");
        model.addAttribute("leaveRequests", leaveRequestService.getAllLeaveRequests());
        return "leave-requests/list";
    }

    @GetMapping("/pending")
    public String listPendingRequests(Model model) {
        model.addAttribute("currentPage", "pending");
        model.addAttribute("leaveRequests", leaveRequestService.getPendingLeaveRequests());
        model.addAttribute("pageTitle", "Pengajuan Menunggu Persetujuan");
        return "leave-requests/list";
    }

    @GetMapping("/new")
    public String showCreateForm(@RequestParam(required = false) Long employeeId, Model model) {
        model.addAttribute("currentPage", "leave-requests");
        model.addAttribute("leaveRequest", new LeaveRequest());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("leaveTypes", LeaveType.values());
        model.addAttribute("selectedEmployeeId", employeeId);
        return "leave-requests/form";
    }

    @PostMapping
    public String createLeaveRequest(@RequestParam Long employeeId,
                                     @ModelAttribute LeaveRequest leaveRequest,
                                     RedirectAttributes redirectAttributes) {
        return employeeService.getEmployeeById(employeeId)
                .map(employee -> {
                    try {
                        leaveRequest.setEmployee(employee);
                        leaveRequestService.createLeaveRequest(leaveRequest);
                        redirectAttributes.addFlashAttribute("success", "Pengajuan cuti berhasil dibuat");
                        return "redirect:/leave-requests";
                    } catch (IllegalArgumentException e) {
                        redirectAttributes.addFlashAttribute("error", e.getMessage());
                        return "redirect:/leave-requests/new?employeeId=" + employeeId;
                    }
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Karyawan tidak ditemukan");
                    return "redirect:/leave-requests/new";
                });
    }

    @GetMapping("/{id}")
    public String viewLeaveRequest(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "leave-requests");
        return leaveRequestService.getLeaveRequestById(id)
                .map(leaveRequest -> {
                    model.addAttribute("leaveRequest", leaveRequest);
                    return "leave-requests/view";
                })
                .orElse("redirect:/leave-requests");
    }

    @PostMapping("/{id}/approve")
    public String approveLeaveRequest(@PathVariable Long id,
                                      @RequestParam(required = false) String approverNotes,
                                      RedirectAttributes redirectAttributes) {
        try {
            leaveRequestService.approveLeaveRequest(id, approverNotes);
            redirectAttributes.addFlashAttribute("success", "Pengajuan cuti berhasil disetujui");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/leave-requests/" + id;
    }

    @PostMapping("/{id}/reject")
    public String rejectLeaveRequest(@PathVariable Long id,
                                     @RequestParam(required = false) String approverNotes,
                                     RedirectAttributes redirectAttributes) {
        try {
            leaveRequestService.rejectLeaveRequest(id, approverNotes);
            redirectAttributes.addFlashAttribute("success", "Pengajuan cuti berhasil ditolak");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/leave-requests/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancelLeaveRequest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            leaveRequestService.cancelLeaveRequest(id);
            redirectAttributes.addFlashAttribute("success", "Pengajuan cuti berhasil dibatalkan");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/leave-requests/" + id;
    }
}
