package id.my.hendisantika.cutiaw.repository;

import id.my.hendisantika.cutiaw.entity.LeaveRequest;
import id.my.hendisantika.cutiaw.entity.LeaveStatus;
import id.my.hendisantika.cutiaw.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployeeId(Long employeeId);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByEmployeeIdAndStatus(Long employeeId, LeaveStatus status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
            "AND lr.leaveType = :leaveType " +
            "AND lr.status = :status " +
            "AND YEAR(lr.startDate) = :year")
    List<LeaveRequest> findByEmployeeIdAndLeaveTypeAndStatusAndYear(
            @Param("employeeId") Long employeeId,
            @Param("leaveType") LeaveType leaveType,
            @Param("status") LeaveStatus status,
            @Param("year") int year);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
            "AND lr.status IN ('PENDING', 'APPROVED') " +
            "AND ((lr.startDate BETWEEN :startDate AND :endDate) " +
            "OR (lr.endDate BETWEEN :startDate AND :endDate) " +
            "OR (lr.startDate <= :startDate AND lr.endDate >= :endDate))")
    List<LeaveRequest> findOverlappingLeaveRequests(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(DATEDIFF(lr.endDate, lr.startDate) + 1), 0) " +
            "FROM LeaveRequest lr WHERE lr.employee.id = :employeeId " +
            "AND lr.leaveType = 'ANNUAL' " +
            "AND lr.status = 'APPROVED' " +
            "AND YEAR(lr.startDate) = :year")
    Integer getTotalApprovedAnnualLeaveDays(
            @Param("employeeId") Long employeeId,
            @Param("year") int year);

    List<LeaveRequest> findAllByOrderByCreatedAtDesc();
}
