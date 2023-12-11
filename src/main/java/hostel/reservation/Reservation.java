package hostel.reservation;

import hostel.room.Room;
import jakarta.persistence.*;
import lombok.Data;
import hostel.web.models.User;

import java.sql.Timestamp;

@Data
@Entity
public class Reservation {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "start", nullable = false)
    private Timestamp start;
    @Column(name = "end", nullable = false)
    private Timestamp end;
    @JoinColumn(name = "fk_room", nullable = false)
    @ManyToOne
    private Room room;
    @JoinColumn(name = "fk_user", nullable = false)
    @ManyToOne
    private User user;

    public Reservation () {

    }

    public Reservation (ReservationDTO reservationDTO, Room room, User user) {
        start = reservationDTO.getStart();
        end = reservationDTO.getEnd();
        this.room = room;
        this.user = user;
    }
}
