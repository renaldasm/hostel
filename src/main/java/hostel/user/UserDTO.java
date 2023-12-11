package hostel.user;

import lombok.Data;
import hostel.web.models.User;

@Data
public class UserDTO {
    private String id;
    private String username;

    public UserDTO() {}

    public UserDTO(User user) {
        id = user.getId();
        username = user.getUsername();
    }
}
