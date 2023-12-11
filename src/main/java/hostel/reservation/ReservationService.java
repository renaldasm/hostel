package hostel.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository repository;
    
    public List<Reservation> findAllByRoomId(long roomId) {
        return repository.findAllByRoomId(roomId);
    }
    public Optional<Reservation> findByRoomIdAndId(long roomId, long reservationId) {
        return repository.findByRoomIdAndId(roomId, reservationId);
    }

    public void save(Reservation reservation) {
        repository.saveAndFlush(reservation);
    }
    
    public void delete(Reservation reservation) {
        repository.delete(reservation);
    }
    
    public boolean reservationExists(long roomId, Timestamp start, Timestamp end) {
        return !repository.findByRoom_IdAndRoom_ReservationList_StartGreaterThanEqualAndRoom_ReservationList_StartLessThanEqual(roomId, start, end).isEmpty() ||
        !repository.findByRoom_IdAndRoom_ReservationList_EndGreaterThanEqualAndRoom_ReservationList_EndLessThanEqual(roomId, start, end).isEmpty();
    }
}
