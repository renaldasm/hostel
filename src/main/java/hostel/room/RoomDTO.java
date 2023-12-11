package hostel.room;
import hostel.user.UserDTO;
import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private UserDTO user;

    public RoomDTO() {}

    public RoomDTO(Room room) {
        id = room.getId();
        name = room.getName();
        description = room.getDescription();
        price = room.getPrice();
        user = new UserDTO(room.getUser());
    }
    
    public boolean notNull() {
        return name != null && description != null && price != null;
    }
    
    public boolean atLeastOneNotNUll() {
        return name != null || description != null || price != null;
    }
}
