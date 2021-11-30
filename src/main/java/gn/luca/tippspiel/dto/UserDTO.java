package gn.luca.tippspiel.dto;

import gn.luca.tippspiel.model.Role;
import gn.luca.tippspiel.model.Tip;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private List<Integer> points;
    private Set<Tip> tipSet;
    private Collection<Role> roles;
}
