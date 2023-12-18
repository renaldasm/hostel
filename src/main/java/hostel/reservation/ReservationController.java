package hostel.reservation;

import hostel.exceptions.HttpBadRequestException;
import hostel.exceptions.HttpConflictException;
import hostel.exceptions.HttpNoContentException;
import hostel.exceptions.HttpNotFoundException;
import hostel.exceptions.HttpUnprocessableContent;
import hostel.room.Room;
import hostel.room.RoomService;
import hostel.user.UserService;
import hostel.web.models.User;
import hostel.web.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(path = "/hotel/{hotelId}/room/{roomId}/reservation")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getReservations(@PathVariable long hotelId, @PathVariable long roomId) {
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpNotFoundException("Provided room not found"));
        List<Reservation> reservationList = reservationService.findAllByRoomId(roomId);

        if (reservationList.isEmpty()) {
            throw new HttpNoContentException();
        }

        List<ReservationDTO> reservationDTOList = new ArrayList<>();
        for (Reservation reservation : reservationList) {
            reservationDTOList.add(new ReservationDTO(reservation));
        }

        return new ResponseEntity<>(reservationDTOList, HttpStatus.OK);
    }

    @GetMapping(path = "/hotel/{hotelId}/room/{roomId}/reservation/{reservationId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservation(
            @PathVariable long hotelId, @PathVariable long roomId, @PathVariable long reservationId) {
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpNotFoundException("Provided room not found"));
        Reservation reservation = reservationService.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> new HttpNotFoundException("Reservation id - " + reservationId));
        return new ResponseEntity<>(new ReservationDTO(reservation), HttpStatus.OK);
    }

    @PostMapping(path = "/hotel/{hotelId}/room/{roomId}/reservation")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<String> addReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long hotelId, @PathVariable long roomId,
                                                 @RequestBody ReservationDTO reservationDTO) {
        if (!reservationDTO.notNull()) {
            throw new HttpUnprocessableContent();
        }
        String token = "";
        String[] authorizationHeaderArray = authorizationHeader.split(" ");
        if (authorizationHeaderArray.length == 2 && "Bearer".equals(authorizationHeaderArray[0])) {
            token = authorizationHeaderArray[1];
        }
        else
        {
            return ResponseEntity.badRequest().body("Authorization token wrong");
        }
        String userId = jwtUtils.getUserIdFromJwtToken(token);

        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpBadRequestException("Provided room not found"));
        User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpBadRequestException("Error with user"));

        if (reservationService.reservationExists(roomId, reservationDTO.getStart(), reservationDTO.getEnd())) {
            throw new HttpConflictException("Reservation between " + reservationDTO.getStart() + " and " + reservationDTO.getEnd());
        }
        Reservation reservation = new Reservation(reservationDTO, room, user); //TODO: Retrieve from session
        reservationService.save(reservation);

        return new ResponseEntity<>("Reservation created with ID: " + reservation.getId(), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/hotel/{hotelId}/room/{roomId}/reservation/{reservationId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<String> updateReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long hotelId, @PathVariable long roomId,
                                                    @PathVariable long reservationId,
                                                    @RequestBody ReservationDTO reservationDTO) {
        if (!reservationDTO.atLeastOneNotNull()) {
            throw new HttpUnprocessableContent();
        }
        Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                .orElseThrow(() -> new HttpBadRequestException("Provided room not found"));
        hostel.web.models.User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpBadRequestException("Error with user"));
        Reservation reservation = reservationService.findByRoomIdAndId(roomId, reservationId)
                .orElseThrow(() -> new HttpBadRequestException("Provided reservation not found"));
        
        reservation.setStart(reservationDTO.getStart() != null ? reservationDTO.getStart() : reservation.getStart());
        reservation.setEnd(reservationDTO.getEnd() != null ? reservationDTO.getEnd() : reservation.getEnd());
        
        if (reservation.getStart().getTime() > reservation.getEnd().getTime()) {
            throw new HttpBadRequestException("Provided reservation start date is after end date");
        }
        
        reservationService.save(reservation);
        return new ResponseEntity<>("Successfully updated reservation", HttpStatus.OK);
    }
    
    @DeleteMapping(path = "/hotel/{hotelId}/room/{roomId}/reservation/{reservationId}")
    @PreAuthorize("hasRole('CLIENT') or hasRole('WORKER') or hasRole('ADMIN')")
    public ResponseEntity<String> deleteReservation(@RequestHeader("Authorization") String authorizationHeader, @PathVariable long hotelId,
                                                    @PathVariable long roomId,
                                                    @PathVariable long reservationId) {
        String token = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String[] authorizationHeaderArray = authorizationHeader.split(" ");
        if (authorizationHeaderArray.length == 2 && "Bearer".equals(authorizationHeaderArray[0])) {
            token = authorizationHeaderArray[1];
        }
        else
        {
            return ResponseEntity.badRequest().body("Authorization token wrong");
        }
        if ( authentication.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()) || "ROLE_WORKER".equals(role.getAuthority())))
        {
            String userId = jwtUtils.getUserIdFromJwtToken(token);
            Room room = roomService.findByHotelIdAndRoomId(hotelId, roomId)
                    .orElseThrow(() -> new HttpNotFoundException("Provided room not found"));
            hostel.web.models.User user = userService.findById(String.valueOf(1)).orElseThrow(() -> new HttpNotFoundException("Error with user"));
            Reservation reservation = reservationService.findByRoomIdAndId(roomId, reservationId)
                    .orElseThrow(() -> new HttpNotFoundException("Provided reservation not found"));

            reservationService.delete(reservation);
            return new ResponseEntity<>("Successfully deleted reservation", HttpStatus.OK);
        }
        return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);

    }
}
