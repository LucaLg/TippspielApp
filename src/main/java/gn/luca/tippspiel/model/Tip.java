package gn.luca.tippspiel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;

@Entity
@Table(name = "tipTable")
@Getter @Setter @NoArgsConstructor
public class Tip {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id

    private long id;
    private long matchId;
    private String hometeam;
    private String awayteam;
    private int tipHomeScore;
    private int tipAwayScore;
    @Value("-1")
    private int actHomeScore ;
    @Value("-1")
    private int actAwayScore ;
    private int points;
    private long matchDayID;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id" ,nullable = false)
    private User user;
    public Tip(long matchId, int tipHomeScore, int tipAwayScore,User user,long matchDayID,String hometeam,String awayteam) {
        this.matchId = matchId;
        this.tipHomeScore = tipHomeScore;
        this.tipAwayScore = tipAwayScore;
        this.user = user;
        this.matchDayID = matchDayID;
        this.hometeam = hometeam;
        this.awayteam = awayteam;

    }
}
