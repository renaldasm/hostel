package hostel.room;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    public List<Room> findByHotelId(long hotelId);
    public Optional<Room> findByHotelIdAndId(long hotel_id, long id);
    
}
