package gn.luca.tippspiel.service;

import gn.luca.tippspiel.dto.RoleDTO;
import gn.luca.tippspiel.dto.UserDTO;
import gn.luca.tippspiel.model.Role;
import gn.luca.tippspiel.model.User;
import gn.luca.tippspiel.repository.RoleRepo;
import gn.luca.tippspiel.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final RoleRepo roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUser() {
        List<User> userList = userRepo.findAll();
        List<UserDTO> userDTOS = new ArrayList<UserDTO>();
        for (User userInList : userList) {
            userInList.updatePoints(0);
            userRepo.save(userInList);
            userDTOS.add(convertEntityToDto(userInList));
        }
        return userDTOS;
    }

    public UserDTO getUserById(Long id) {
        return convertEntityToDto(userRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not Found")
        ));
    }

    public UserDTO getUserByName(String name) {

        User userToFind = userRepo.findByUsername(name).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
        return convertEntityToDto(userToFind);
    }

    public UserDTO deleteUser(Long id) {
        UserDTO userToDelete =
                convertEntityToDto(userRepo.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("User to delete not Found")));
        userRepo.deleteById(id);
        return userToDelete;
    }

    public UserDTO createUser(UserDTO user, String password) {
        if (userRepo.findByUsername(user.getUsername()).isPresent()) {
            return null;
        }
        User newUser = new User(user.getUsername(), password, new ArrayList<Integer>(), new ArrayList<>());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.getRoles().add(roleRepo.findByName("User"));
        userRepo.save(newUser);
        return convertEntityToDto(newUser);
    }

    public boolean isPresent(String username) {
        return userRepo.findByUsername(username).isPresent();
    }

    public UserDTO updateUser(UserDTO newUserDTO, long id) {
        User newUser = new User(newUserDTO.getUsername(), newUserDTO.getPassword(), newUserDTO.getPoints(), newUserDTO.getRoles());
        newUserDTO.getTipSet().forEach(newUser::addNewTip);
        userRepo.deleteById(id);
        return convertEntityToDto(userRepo.save(newUser));
    }

    public RoleDTO saveRole(RoleDTO role) {
        return roleToDTO(roleRepo.save(new Role(role.getName())));
    }

    public RoleDTO addRoleToUser(long userId, String rolename) {
        User user = userRepo.findById(userId).orElseThrow(() -> {
            return new EntityNotFoundException("User not found");
        });
        if (!user.getRoles().contains(roleRepo.findByName(rolename))) {
            user.getRoles().add(roleRepo.findByName(rolename));
            userRepo.save(user);
        }

        return roleToDTO(user.getRoles()
                                .stream()
                                .filter(role -> role.getName().equals(rolename))
                                .collect(Collectors.toList()).get(0));
    }

    private UserDTO convertEntityToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setPoints(user.getPoints());
        userDTO.setTipSet(user.getTips());
        userDTO.setRoles(user.getRoles());

        return userDTO;
    }

    private RoleDTO roleToDTO(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByUsername(username).get();
        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
