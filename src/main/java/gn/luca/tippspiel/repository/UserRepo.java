package gn.luca.tippspiel.repository;

import gn.luca.tippspiel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
   Optional<User>  findByUsername(String username);
}
