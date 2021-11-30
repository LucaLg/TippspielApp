package gn.luca.tippspiel.service;

import gn.luca.tippspiel.dto.TipDTO;
import gn.luca.tippspiel.model.Tip;
import gn.luca.tippspiel.model.User;
import gn.luca.tippspiel.repository.TipRepo;
import gn.luca.tippspiel.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TipService {
    @Autowired
    private TipRepo tipRepo;
    @Autowired
    private UserRepo userRepo;

    public Set<TipDTO> getTipsByUsername(String username) {
        Set<TipDTO> newSet = new HashSet<>();
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        if (user != null && !user.getTips().isEmpty()) {

            for (int i = user.getTips().size() - 1; i > user.getTips().size() - 10; i--) {
                newSet.add(toDTO((Tip) user.getTips().toArray()[i]));
            }
        }
        if (newSet.isEmpty()) {
            return null;
        }
        return newSet;
    }

    public TipDTO getTipByMatchId(String username, long matchId) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
//        if(user != null && !user.getTips().isEmpty()){
//            for(Tip userTip: user.getTips()){
//                if(userTip.getMatchId() == matchId){
//                    return toDTO(userTip);
//                }
//            }
//
//        }

        Tip tip = tipRepo.findByMatchIdAndUser(matchId, user).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
        return toDTO(tip);


    }

    public TipDTO createTip(TipDTO tipDTO, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        Tip tip = new Tip(tipDTO.getMatchId(), tipDTO.getTipHomeScore(), tipDTO.getTipAwayScore(), user, tipDTO.getMatchDayID(),tipDTO.getHometeam(),tipDTO.getAwayteam());
        //userRepo.getById(userId).addNewTip(tip);
        tipRepo.save(tip);
        return toDTO(tipRepo.findById(tip.getId()).get());
    }

    public TipDTO updateTip(TipDTO tipDTO, String user) {

        Tip tipUp = tipRepo.findByMatchIdAndUser(tipDTO.getMatchId(), userRepo.findByUsername(user).get()).get();
        tipUp.setTipAwayScore(tipDTO.getTipAwayScore());
        tipUp.setTipHomeScore(tipDTO.getTipHomeScore());
        tipUp.setActAwayScore(tipDTO.getActAwayScore());
        tipUp.setActHomeScore(tipDTO.getActHomeScore());
        tipUp.setPoints(tipDTO.getPoints());
        //System.out.println("Update Tip!!!!!" + tipUp.getActHomeScore() + " " +  tipUp.getActAwayScore());
        tipRepo.save(tipUp);
        return toDTO(tipUp);
    }

    public TipDTO updateActScore(TipDTO newTip, long matchId, String username) {
        return null;
    }

    public List<TipDTO> getTipsByMatchDay(String username, long matchDayID) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        return tipRepo.findByMatchDayIDAndUser(matchDayID, user).stream().map(this::toDTO).collect(Collectors.toList());
    }
    public Set<Long> getAllMatchWeekIDs(String username){
        Set<Long> matchweekSet = new HashSet<>();
        getAllTipsByUsername(username).forEach(tipDTO -> matchweekSet.add(tipDTO.getMatchDayID()));
        return matchweekSet ;
    }
    public List<TipDTO> getAllTipsByUsername(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });

        return tipRepo.findAllByUser(user).orElseThrow(()-> new EntityNotFoundException("No Tips on this MatchWeek!")).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Set<TipDTO> saveTipList(TipDTO[] tipList, String username) {
        Set<Tip> newTips = new HashSet<Tip>();
        User user = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        TipDTO newTipDto;
        for (TipDTO tip : tipList) {
            if (tipRepo.findByMatchIdAndUser(tip.getMatchId(), user).isPresent()) {
                newTipDto = updateTip(tip, username);

            } else {
                newTipDto = createTip(tip, username);

            }
            Tip newTip = new Tip(newTipDto.getMatchId(), newTipDto.getTipHomeScore(), newTipDto.getTipAwayScore(), userRepo.findByUsername(tip.getUser()).get(), newTipDto.getMatchDayID(),newTipDto.getHometeam(),newTipDto.getAwayteam());
            newTip.setPoints(newTipDto.getPoints());

            newTip.setActHomeScore(newTipDto.getActHomeScore());
            newTip.setActAwayScore(newTipDto.getActAwayScore());
            newTips.add(newTip);

        }
//        for (TipDTO tip : tipList) {
//            boolean inList = user.getTips().stream().anyMatch(tip1 -> {
//                return tip1.getMatchId() == tip.getMatchId();
//            });
//            if (!inList) {
//                newTips.add(new Tip(tip.getMatchId(), tip.getTipHomeScore(), tip.getTipAwayScore(),userRepo.findByUsername(tip.getUser()),tip.getMatchDayID()));
//            }else{
//                user.getTips().forEach(tip1 -> {
//                    if(tip1.getMatchId() == tip.getMatchId()){
//                        tip1.setTipHomeScore(tip.getTipHomeScore());
//                        tip1.setTipAwayScore(tip.getTipAwayScore());
//                    }
//                });
//            }
//        }
//        user.addNewTips(newTips);
//        userRepo.save(user);

        return newTips.stream().map(this::toDTO).collect(Collectors.toSet());
    }

    private TipDTO toDTO(Tip tip) {
        TipDTO nTip =  new TipDTO(tip.getMatchId(), tip.getTipHomeScore(), tip.getTipAwayScore(), tip.getMatchId(), tip.getUser().getUsername(), tip.getMatchDayID(),tip.getHometeam(),tip.getAwayteam());
        nTip.setActHomeScore(tip.getActHomeScore());
        nTip.setActAwayScore(tip.getActAwayScore());
        nTip.setPoints(tip.getPoints());
        return nTip;
    }
}
