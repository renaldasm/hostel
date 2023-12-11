package hostel.hotel;

import hostel.room.Room;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import java.util.List;

@Data
@Entity
public class Hotel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Getter
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE)
    private List<Room> rooms;

    public Hotel() {

    }

    public Hotel(HotelDTO hotelDTO) {
        name = hotelDTO.getName();
    }
    
    public void patch(HotelDTO hotelDTO) {
        name = hotelDTO.getName() != null ? hotelDTO.getName() : name;
    }
}
