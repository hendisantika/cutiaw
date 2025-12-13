package id.my.hendisantika.cutiaw.config;

import id.my.hendisantika.cutiaw.entity.Employee;
import id.my.hendisantika.cutiaw.entity.LeaveRequest;
import id.my.hendisantika.cutiaw.entity.LeaveStatus;
import id.my.hendisantika.cutiaw.entity.LeaveType;
import id.my.hendisantika.cutiaw.repository.EmployeeRepository;
import id.my.hendisantika.cutiaw.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public void run(String... args) {
        if (employeeRepository.count() == 0) {
            log.info("Initializing sample data...");

            Employee emp1 = employeeRepository.save(Employee.builder()
                    .employeeId("EMP001")
                    .name("Budi Santoso")
                    .email("budi.santoso@company.com")
                    .department("IT")
                    .position("Software Developer")
                    .joinDate(LocalDate.of(2022, 1, 15))
                    .annualLeaveBalance(12)
                    .build());

            Employee emp2 = employeeRepository.save(Employee.builder()
                    .employeeId("EMP002")
                    .name("Siti Rahayu")
                    .email("siti.rahayu@company.com")
                    .department("HR")
                    .position("HR Manager")
                    .joinDate(LocalDate.of(2021, 6, 1))
                    .annualLeaveBalance(10)
                    .build());

            Employee emp3 = employeeRepository.save(Employee.builder()
                    .employeeId("EMP003")
                    .name("Ahmad Wijaya")
                    .email("ahmad.wijaya@company.com")
                    .department("Finance")
                    .position("Accountant")
                    .joinDate(LocalDate.of(2023, 3, 10))
                    .annualLeaveBalance(12)
                    .build());

            Employee emp4 = employeeRepository.save(Employee.builder()
                    .employeeId("EMP004")
                    .name("Dewi Lestari")
                    .email("dewi.lestari@company.com")
                    .department("Marketing")
                    .position("Marketing Specialist")
                    .joinDate(LocalDate.of(2022, 9, 1))
                    .annualLeaveBalance(8)
                    .build());

            Employee emp5 = employeeRepository.save(Employee.builder()
                    .employeeId("EMP005")
                    .name("Rizky Pratama")
                    .email("rizky.pratama@company.com")
                    .department("IT")
                    .position("System Analyst")
                    .joinDate(LocalDate.of(2020, 4, 20))
                    .annualLeaveBalance(15)
                    .build());

            leaveRequestRepository.save(LeaveRequest.builder()
                    .employee(emp1)
                    .leaveType(LeaveType.ANNUAL)
                    .startDate(LocalDate.now().plusDays(7))
                    .endDate(LocalDate.now().plusDays(10))
                    .reason("Liburan keluarga")
                    .status(LeaveStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build());

            leaveRequestRepository.save(LeaveRequest.builder()
                    .employee(emp2)
                    .leaveType(LeaveType.SICK)
                    .startDate(LocalDate.now().minusDays(3))
                    .endDate(LocalDate.now().minusDays(1))
                    .reason("Demam dan flu")
                    .status(LeaveStatus.APPROVED)
                    .approverNotes("Semoga lekas sembuh")
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .updatedAt(LocalDateTime.now().minusDays(4))
                    .build());

            leaveRequestRepository.save(LeaveRequest.builder()
                    .employee(emp3)
                    .leaveType(LeaveType.MARRIAGE)
                    .startDate(LocalDate.now().plusDays(30))
                    .endDate(LocalDate.now().plusDays(32))
                    .reason("Pernikahan")
                    .status(LeaveStatus.PENDING)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());

            leaveRequestRepository.save(LeaveRequest.builder()
                    .employee(emp4)
                    .leaveType(LeaveType.ANNUAL)
                    .startDate(LocalDate.now().plusDays(14))
                    .endDate(LocalDate.now().plusDays(16))
                    .reason("Pulang kampung")
                    .status(LeaveStatus.REJECTED)
                    .approverNotes("Mohon ajukan di waktu lain karena ada deadline project")
                    .createdAt(LocalDateTime.now().minusDays(7))
                    .updatedAt(LocalDateTime.now().minusDays(6))
                    .build());

            leaveRequestRepository.save(LeaveRequest.builder()
                    .employee(emp5)
                    .leaveType(LeaveType.BEREAVEMENT)
                    .startDate(LocalDate.now().minusDays(10))
                    .endDate(LocalDate.now().minusDays(8))
                    .reason("Keluarga meninggal")
                    .status(LeaveStatus.APPROVED)
                    .approverNotes("Turut berduka cita")
                    .createdAt(LocalDateTime.now().minusDays(12))
                    .updatedAt(LocalDateTime.now().minusDays(11))
                    .build());

            log.info("Sample data initialized successfully!");
        }
    }
}
