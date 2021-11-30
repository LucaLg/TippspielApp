package gn.luca.tippspiel.controller;

import com.sun.xml.bind.v2.TODO;
import gn.luca.tippspiel.dto.GroupDTO;
import gn.luca.tippspiel.dto.UserDTO;
import gn.luca.tippspiel.model.User;
import gn.luca.tippspiel.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping(value = "group")
@CrossOrigin(origins = "*")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping()
    public List<GroupDTO> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping(value = "{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GroupDTO getGroupById(@PathVariable long groupId) {
        GroupDTO groupDTO = groupService.getGroupById(groupId);
        return groupDTO;
    }

    @GetMapping(value = "/groupName/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    GroupDTO getGroupById(@PathVariable String name) {
        GroupDTO groupDTO = groupService.getGroupByName(name);
        return groupDTO;
    }

    @PostMapping(value = "createGroup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO ) {
       return ResponseEntity.ok().body( groupService.createGroup(groupDTO));
    }

    @DeleteMapping(value = "deleteGroup/{groupId}")
    public void deleteGroup(@PathVariable long groupId) {
        groupService.deleteGroup(groupId);
    }

    @GetMapping(value = "{groupId}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<List<String>> getAllUserByGroup(@PathVariable long groupId) {

        return ResponseEntity.ok().body(groupService.getUsers(groupId));
    }
    @PutMapping(value = "{groupId}/{username}")
    public ResponseEntity<GroupDTO> addUser(@PathVariable long groupId,@PathVariable String username){
       return ResponseEntity.ok().body(groupService.addUser(groupId,username));
    }
    @DeleteMapping(value = "{groupId}/{username}")
    public ResponseEntity<GroupDTO> deleteUser(@PathVariable long groupId,@PathVariable String username){
        return ResponseEntity.ok().body(groupService.removeUser(groupId,username));
    }

}
