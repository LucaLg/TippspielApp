package gn.luca.tippspiel.repository;

import gn.luca.tippspiel.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepo extends JpaRepository<Group, Long> {
   public Optional<Group> findByName(String name);
}
