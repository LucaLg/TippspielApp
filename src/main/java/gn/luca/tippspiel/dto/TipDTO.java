package gn.luca.tippspiel.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter @Setter @NoArgsConstructor
public class TipDTO {
    private long id;


    private long matchId;
    private String hometeam;
    private String awayteam;
    private int tipHomeScore;
    private int tipAwayScore;
    @Value("-1")
    private int actHomeScore;
    @Value("-1")
    private int actAwayScore;
    private int points;
    private String user;
    private long matchDayID;
    public TipDTO(long id , int tipHomeScore,int tipAwayScore,long matchId,String user,long matchDayID,String hometeam,String awayteam){
        this.id = id;
        this.tipHomeScore = tipHomeScore;
        this.tipAwayScore = tipAwayScore;
        this.matchId = matchId;
        this.user = user;
        this.matchDayID = matchDayID;
        this.hometeam = hometeam;
        this.awayteam = awayteam;
    }
}
