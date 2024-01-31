package tacos.data;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import tacos.AppUser;
import tacos.authorization.User;
public interface UserRepository extends CrudRepository<AppUser, Long> {
 AppUser findByUsername(String username);
//Optional<AppUser> findByUsername(String username);

//void save(User user);
}