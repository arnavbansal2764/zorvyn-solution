package github.arnavbansal2764.zorvyn_solution.repository;

import github.arnavbansal2764.zorvyn_solution.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
