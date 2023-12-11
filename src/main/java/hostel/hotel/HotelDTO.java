package hostel.hotel;

import hostel.room.RoomDTO;
import lombok.Data;

import java.util.List;

@Data
public class HotelDTO {
    private Long id;
    private String name;
    private List<RoomDTO> rooms;

    public HotelDTO() {}

    public HotelDTO(Hotel hotel, List<RoomDTO> rooms) {
        id = hotel.getId();
        name = hotel.getName();
        this.rooms = rooms;
    }
    
    public boolean notNull() {
        return name != null;
    }
    
    public boolean atleastOneNotNull() {
        return name != null;
    }
}
