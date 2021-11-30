package gn.luca.tippspiel.controller;

import com.sun.istack.NotNull;
import gn.luca.tippspiel.dto.TipDTO;

import gn.luca.tippspiel.model.Tip;
import gn.luca.tippspiel.service.TipService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping( "tips")
@CrossOrigin(origins = "*")
public class TipController {
    @Autowired
    private TipService tipService;

    @GetMapping(value = "{username}")
    public ResponseEntity<List<TipDTO>> getTipsByUsername(@PathVariable @NotNull String username){
        return  ResponseEntity.ok().body(tipService.getAllTipsByUsername(username));
    }
    @GetMapping(value = "{matchId}/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipDTO> getTipByMatchIdAndUsername(@PathVariable @NotNull String username,@PathVariable long matchId){
        return ResponseEntity.ok().body(tipService.getTipByMatchId(username,matchId));
    }
    @GetMapping(value = "/matchweek/{username}/{matchDayID}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TipDTO>> getTipsByMatchDayIDAndUsername(@PathVariable String username,@PathVariable long matchDayID){
        return ResponseEntity.ok().body(tipService.getTipsByMatchDay(username,matchDayID));
    }
    @GetMapping(value = "/matchweek/{username}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Long>> getAllMatchWeekIds(@PathVariable String username){
        return ResponseEntity.ok().body(tipService.getAllMatchWeekIDs(username));
    }
    @PostMapping(value = "/{username}/createTip" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TipDTO> createTip(@RequestBody TipDTO tipDTO,@PathVariable String username){
        return ResponseEntity.ok().body(tipService.createTip(tipDTO,username));
    }
    @PutMapping(value = "{username}" ,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<TipDTO> updateTip(@RequestBody TipDTO newTip,@PathVariable String username){
        return  ResponseEntity.ok().body(tipService.updateTip(newTip,username));
    }
    @PostMapping(value = "/{username}/saveTips" ,consumes = MediaType.APPLICATION_JSON_VALUE ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<TipDTO>> saveTips(@RequestBody TipDTO[] newTips,@PathVariable String username){
        return ResponseEntity.ok().body(tipService.saveTipList(newTips,username));
    }

}
