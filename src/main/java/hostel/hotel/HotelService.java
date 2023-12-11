package hostel.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    @Autowired
    private HotelRepository repository;
    
    public List<Hotel> findAll() {
        return repository.findAll();
    }
    
    public Optional<Hotel> findById(long id) {
        return repository.findById(id);
    }
    
    public Optional<Hotel> findByName(String name) {
        return repository.findByName(name);
    }
    
    public void save(Hotel hotel) {
        repository.saveAndFlush(hotel);
    }
    
    public void delete(Hotel hotel) {
        repository.delete(hotel);
    }
}
