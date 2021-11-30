package gn.luca.tippspiel.dto;

import gn.luca.tippspiel.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;

@Getter
@Setter
public class GroupDTO {
    private long id;
    private String name;
    private Set<String> users;
}
