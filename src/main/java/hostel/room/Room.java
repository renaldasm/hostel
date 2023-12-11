package hostel.room;

import hostel.hotel.Hotel;
import hostel.web.models.User;
import hostel.reservation.Reservation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Room {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", nullable = true, length = 100)
    private String description;
    @Column(name = "price", nullable = false)
    private int price;
    @JoinColumn(name = "fk_hotel", nullable = false)
    @ManyToOne
    private Hotel hotel;
    @JoinColumn(name = "fk_user", nullable = false)
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Reservation> reservationList;

    public Room() {}

    public Room(RoomDTO roomDTO, Hotel hotel, User user) {
        name = roomDTO.getName();
        description = roomDTO.getDescription();
        price = roomDTO.getPrice();
        this.hotel = hotel;
        this.user = user;
    }
    
    public void patch(RoomDTO roomDTO) {
        name = roomDTO.getName() != null ? roomDTO.getName() : name;
        description = roomDTO.getDescription() != null ? roomDTO.getDescription() : description;
        price = roomDTO.getPrice() != null ? roomDTO.getPrice() : price;
    }
}