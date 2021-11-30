package gn.luca.tippspiel.repository;

import gn.luca.tippspiel.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {
    public Role findByName(String name);
}
