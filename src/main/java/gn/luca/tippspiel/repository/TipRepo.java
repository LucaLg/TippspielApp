package gn.luca.tippspiel.repository;

import gn.luca.tippspiel.model.Tip;
import gn.luca.tippspiel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TipRepo extends JpaRepository<Tip,Long> {
    public Optional<Tip> findByMatchIdAndUser(long matchId, User user);
    public List<Tip> findByMatchDayIDAndUser(long matchDay, User user);
    public Optional<List<Tip>> findAllByUser(User user);

}
