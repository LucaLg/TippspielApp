package gn.luca.tippspiel.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "groupTable")
public class Group implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false,updatable = false)
    private long id;
    private String name;
    @ManyToMany
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))

    private Set<User> userList;
    public Group(String name){
        this.name = name;
    }
    public void addUser(final User newUser){
        if(Objects.isNull((userList))){
            userList = new HashSet<>();
        }
        this.userList.add(newUser);
    }
    public void removeUser(final User deleteUser){
        userList.remove(deleteUser);
    }
    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\''+
                '}';
    }
}
