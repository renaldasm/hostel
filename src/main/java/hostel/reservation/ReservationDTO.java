package hostel.reservation;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReservationDTO {
    private long id;
    private Timestamp start;
    private Timestamp end;

    public ReservationDTO() {}

    public ReservationDTO(Reservation reservation) {
        id = reservation.getId();
        start = reservation.getStart();
        end = reservation.getEnd();
    }
    
    public boolean notNull() {

        return start != null && end != null;
    }
    public boolean atLeastOneNotNull() {
        return start != null || end != null;
    }
}
