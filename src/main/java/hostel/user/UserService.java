package hostel.user;

import hostel.web.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hostel.web.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository repository;
    
    public List<hostel.web.models.User> findAll(){
        return repository.findAll();
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }
}
