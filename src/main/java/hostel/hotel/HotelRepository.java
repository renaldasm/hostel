package hostel.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    
    public Optional<Hotel> findByName(String name);
}
