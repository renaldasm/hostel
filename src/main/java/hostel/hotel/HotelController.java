package hostel.hotel;
import hostel.exceptions.HttpBadRequestException;
import hostel.exceptions.HttpConflictException;
import hostel.exceptions.HttpNotFoundException;
import hostel.exceptions.HttpUnprocessableContent;
import hostel.room.Room;
import hostel.room.RoomDTO;
import hostel.room.RoomService;
import hostel.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class HotelController {
    
    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;
    
    @GetMapping(path = "/hotel")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<List<HotelDTO>> getHotels() {
        List<Hotel> hotels = hotelService.findAll();
        List<HotelDTO> hotelDTOList = new ArrayList<>();

        for (Hotel hotel : hotels) {
            List<Room> roomList = roomService.findByHotelId(hotel.getId());
            List<RoomDTO> roomDTOList = new ArrayList<>();

            for (Room room : roomList) {
                roomDTOList.add(new RoomDTO(room));
            }
            hotelDTOList.add(new HotelDTO(hotel, roomDTOList));
        }
        return new ResponseEntity<>(hotelDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/hotel/{id}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<HotelDTO> getHotel(@PathVariable long id) {
        Optional<Hotel> _hotel = hotelService.findById(id);

        if (!_hotel.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        Hotel hotel = _hotel.get();
        List<Room> roomList = roomService.findByHotelId(hotel.getId());
        List<RoomDTO> roomDTOList = new ArrayList<>();
        for (Room room : roomList) {
            roomDTOList.add(new RoomDTO(room));
        }
        return new ResponseEntity<>(new HotelDTO(hotel, roomDTOList), HttpStatus.OK);
    }
    
    @PostMapping(path = "/hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addHotel(@RequestBody HotelDTO hotelDTO) {
        if (!hotelDTO.notNull()) {
            throw new HttpUnprocessableContent();
        }
        Optional<Hotel> existingHotel = hotelService.findByName(hotelDTO.getName());
        if (existingHotel.isPresent()) {
            throw new HttpConflictException(hotelDTO.getName());
        }
        Hotel hotelToAdd = new Hotel(hotelDTO);
        hotelService.save(hotelToAdd);
        
        return new ResponseEntity<>("Hotel created with ID: " + hotelToAdd.getId(), HttpStatus.CREATED);
    }
    
    @PatchMapping(path = "/hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateHotel(@PathVariable long hotelId, @RequestBody HotelDTO hotelDTO) {
        if (!hotelDTO.atleastOneNotNull()) {
            throw new HttpUnprocessableContent();
        }
        
        Hotel hotel = hotelService.findById(hotelId).orElseThrow(() -> new HttpBadRequestException("Provided hotel not found"));
        hotel.patch(hotelDTO);
        hotelService.save(hotel);
        return new ResponseEntity<>("Successfully updated hotel", HttpStatus.OK);
    }
    
    @DeleteMapping(path = "hotel/{hotelId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@PathVariable long hotelId) {
        Hotel hotel = hotelService.findById(hotelId).orElseThrow(() -> new HttpNotFoundException("hotel"));
        hotelService.delete(hotel);
        return new ResponseEntity<>("Successfully deleted hotel", HttpStatus.OK);
    }
}
