package gn.luca.tippspiel.controller;

import com.sun.istack.NotNull;
import gn.luca.tippspiel.dto.RoleDTO;
import gn.luca.tippspiel.dto.UserDTO;
import gn.luca.tippspiel.model.Role;
import gn.luca.tippspiel.model.User;
import gn.luca.tippspiel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public @ResponseBody

    ResponseEntity<List<UserDTO>> getAllUser() {
        return ResponseEntity.ok().body(userService.getAllUser());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable long id) {

        return ResponseEntity.ok().body(userService.getUserById(id));
    }

    @GetMapping(value = "/name/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByName(@PathVariable String username) {

        return ResponseEntity.ok().body(userService.getUserByName(username));
    }

    @GetMapping(value = "/present/{username}")
    public boolean isUserPresent(@PathVariable String username) {
        return userService.isPresent(username);
    }

    @PostMapping(value = "createUser",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity createUser(@NotNull @RequestBody UserDTO userDTO, @RequestParam(name = "pwd") final String base64password) {
        String password = new String(Base64.getDecoder().decode(base64password));
        UserDTO newUser = userService.createUser(userDTO, password);
        if(newUser == null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken!");
        }
        return ResponseEntity.ok().body(newUser);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void deleteUser(@NotNull @PathVariable long id) {
        userService.deleteUser(id);
    }

    @PutMapping(value = "update/{id}")
    public ResponseEntity<UserDTO> updateUser(@NotNull @RequestBody UserDTO userDTO, @PathVariable long id) {

        return ResponseEntity.ok().body(userService.updateUser(userDTO, id));

    }

    @PostMapping(value = "{userId}/role", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> addRoleToUser(@RequestBody RoleDTO role, @PathVariable long userId) {
        return ResponseEntity.ok().body(userService.addRoleToUser(userId, role.getName()));
    }

    @PostMapping(value = "role/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> saveRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok().body(userService.saveRole(roleDTO));
    }

}
