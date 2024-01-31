package tacos.authorization;

import org.springframework.data.repository.CrudRepository;

public interface AuthUserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
}
