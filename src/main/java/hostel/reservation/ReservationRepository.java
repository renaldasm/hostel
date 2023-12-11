package hostel.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByRoomId(long room_id);
    Optional<Reservation> findByRoomIdAndId(long room_id, long id);
    List<Reservation> findByRoom_IdAndRoom_ReservationList_StartGreaterThanEqualAndRoom_ReservationList_StartLessThanEqual(long id, Timestamp start, Timestamp end);
    List<Reservation> findByRoom_IdAndRoom_ReservationList_EndGreaterThanEqualAndRoom_ReservationList_EndLessThanEqual(long id, Timestamp start, Timestamp end);
    
    
}
