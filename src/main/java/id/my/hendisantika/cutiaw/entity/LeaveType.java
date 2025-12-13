package id.my.hendisantika.cutiaw.entity;

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
public enum LeaveType {
    ANNUAL("Cuti Tahunan"),
    SICK("Cuti Sakit"),
    MATERNITY("Cuti Melahirkan"),
    PATERNITY("Cuti Ayah"),
    MARRIAGE("Cuti Menikah"),
    BEREAVEMENT("Cuti Duka"),
    UNPAID("Cuti Tanpa Gaji");

    private final String displayName;

    LeaveType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
