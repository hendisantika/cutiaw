package id.my.hendisantika.cutiaw.service;

import id.my.hendisantika.cutiaw.entity.Employee;
import id.my.hendisantika.cutiaw.entity.LeaveRequest;
import id.my.hendisantika.cutiaw.entity.LeaveStatus;
import id.my.hendisantika.cutiaw.entity.LeaveType;
import id.my.hendisantika.cutiaw.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
@RequiredArgsConstructor
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeService employeeService;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id);
    }

    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findByStatus(LeaveStatus.PENDING);
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        validateLeaveRequest(leaveRequest);
        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest approveLeaveRequest(Long id, String approverNotes) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be approved");
        }

        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setApproverNotes(approverNotes);

        if (leaveRequest.getLeaveType() == LeaveType.ANNUAL) {
            employeeService.updateLeaveBalance(
                    leaveRequest.getEmployee().getId(),
                    (int) leaveRequest.getTotalDays()
            );
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest rejectLeaveRequest(Long id, String approverNotes) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only pending requests can be rejected");
        }

        leaveRequest.setStatus(LeaveStatus.REJECTED);
        leaveRequest.setApproverNotes(approverNotes);

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest cancelLeaveRequest(Long id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));

        if (leaveRequest.getStatus() == LeaveStatus.APPROVED &&
                leaveRequest.getLeaveType() == LeaveType.ANNUAL) {
            employeeService.restoreLeaveBalance(
                    leaveRequest.getEmployee().getId(),
                    (int) leaveRequest.getTotalDays()
            );
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        return leaveRequestRepository.save(leaveRequest);
    }

    private void validateLeaveRequest(LeaveRequest leaveRequest) {
        if (leaveRequest.getStartDate().isAfter(leaveRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        if (leaveRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past");
        }

        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingLeaveRequests(
                leaveRequest.getEmployee().getId(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate()
        );

        if (!overlapping.isEmpty()) {
            throw new IllegalArgumentException("Leave request overlaps with existing request");
        }

        if (leaveRequest.getLeaveType() == LeaveType.ANNUAL) {
            Employee employee = leaveRequest.getEmployee();
            long requestedDays = leaveRequest.getTotalDays();
            if (requestedDays > employee.getAnnualLeaveBalance()) {
                throw new IllegalArgumentException(
                        "Insufficient leave balance. Available: " + employee.getAnnualLeaveBalance() +
                                " days, Requested: " + requestedDays + " days"
                );
            }
        }
    }

    public int getRemainingAnnualLeave(Long employeeId) {
        return employeeService.getEmployeeById(employeeId)
                .map(Employee::getAnnualLeaveBalance)
                .orElse(0);
    }
}
