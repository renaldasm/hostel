package hostel.web.repository;

import hostel.web.models.ERole;
import hostel.web.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
