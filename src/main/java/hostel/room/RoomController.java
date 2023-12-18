package hostel.room;

import hostel.hotel.Hotel;
import hostel.hotel.HotelService;
import hostel.web.models.User;
import hostel.exceptions.HttpBadRequestException;
import hostel.exceptions.HttpNoContentException;
import hostel.exceptions.HttpNotFoundException;
import hostel.exceptions.HttpUnprocessableContent;
import hostel.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @GetMapping(path = "/hotel/{hotelId}/room")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<List<RoomDTO>> getAllHotelRooms(@PathVariable long hotelId) {
        List<Room> roomList = roomService.findByHotelId(hotelId);
        List<RoomDTO> roomDTOList = new ArrayList<>();
        
        for (Room room : roomList) {
            roomDTOList.add(new RoomDTO(room));
        }
        return new ResponseEntity<>(roomDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/hotel/{hotelId}/room/{roomId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable long hotelId, @PathVariable long roomId) {
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId).orElseThrow(HttpNoContentException::new);
        return new ResponseEntity<>(new RoomDTO(room), HttpStatus.OK);
    }
    
    @PostMapping(path = "/hotel/{hotelId}/room")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<String> addRoom(@PathVariable long hotelId, @RequestBody RoomDTO roomDTO) {
        if (!roomDTO.notNull()) {
            throw new HttpUnprocessableContent();
        }
        
        Hotel hotel = hotelService.findById(hotelId).orElseThrow(() -> new HttpNotFoundException("room"));
        User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpBadRequestException("User error"));
        Room room = new Room(roomDTO, hotel, user);
        roomService.save(room);
        return new ResponseEntity<>("Room created with ID: " + room.getId(), HttpStatus.CREATED);
    }
    
    @PatchMapping(path = "/hotel/{hotelId}/room/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<String> updateRoom(@PathVariable long hotelId,
                                             @PathVariable long roomId,
                                             @RequestBody RoomDTO roomDTO) {
        if (!roomDTO.atLeastOneNotNUll()) {
            throw new HttpUnprocessableContent();
        }
        
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new HttpNotFoundException("Provided hotel not found"));
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpNotFoundException("Provided room not found"));
        User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpBadRequestException("User error"));
        room.patch(roomDTO);
        roomService.save(room);
        return new ResponseEntity<>("Successfully updated room", HttpStatus.OK);
    }
    
    @DeleteMapping(path = "/hotel/{hotelId}/room/{roomId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WORKER')")
    public ResponseEntity<String> deleteRoom(@PathVariable long hotelId, @PathVariable long roomId) {
        Hotel hotel = hotelService.findById(hotelId)
                .orElseThrow(() -> new HttpNotFoundException("Provided hotel not found"));
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpNotFoundException("Provided room not found"));
        User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpBadRequestException("User error"));
        roomService.delete(room);
        return new ResponseEntity<>("Successfully deleted room", HttpStatus.OK);
    }
}
