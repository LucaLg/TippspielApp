package gn.luca.tippspiel.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor

@Table(name = "userTable")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    @ElementCollection
    private List<Integer> points;
    @JsonManagedReference
    @OneToMany(mappedBy = "user" ,fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private Set<Tip> tips;
    @ManyToMany(mappedBy = "userList")

    private Set<Group> groups;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Role> roles = new ArrayList<>();
    public User(String username, String password, List<Integer> points,Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.points = points;
        this.roles = roles;
    }
    public void updatePoints(int season){
        int p = tips.stream().mapToInt(Tip::getPoints).sum();
        if(points.size()>0){
            points.set(season,p);
        }else{
            points.add(p);
        }

    }
    public void addNewTips(Set<Tip> newTips){
        newTips.forEach(tip -> {
            this.getTips().add(tip);
        });
    }
   public void addNewTip(Tip tip){
        if(Objects.isNull(tips)){
            tips = new HashSet<>();
            tips.add(tip);
        }else{
            tips.add(tip);
        }
    }
    public void removeTip(Tip tip){
        if(!Objects.isNull(tips)){
            tips.remove(tip);
        }
    }
}
