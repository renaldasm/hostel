package hostel.room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository repository;
    
    public List<Room> findAll() {
        return repository.findAll();
    } 
    
    public List<Room> findByHotelId(long hotelId) {
        return repository.findByHotelId(hotelId);
    }
    
    public Optional<Room> findByHotelIdAndRoomId(long hotelId, long roomId) {
        return repository.findByHotelIdAndId(hotelId, roomId);
    }
    
    public void save(Room room) {
        repository.save(room);
    }
    
    public void delete(Room room) {
        repository.delete(room);
    }
}
