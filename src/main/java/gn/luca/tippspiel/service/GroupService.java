package gn.luca.tippspiel.service;

import gn.luca.tippspiel.dto.GroupDTO;
import gn.luca.tippspiel.dto.UserDTO;
import gn.luca.tippspiel.model.Group;
import gn.luca.tippspiel.model.User;
import gn.luca.tippspiel.repository.GroupRepo;
import gn.luca.tippspiel.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    GroupRepo groupRepo;
    @Autowired
    UserRepo userRepo;

    public GroupService(GroupRepo groupRepo, UserRepo userRepo) {
        this.groupRepo = groupRepo;
        this.userRepo = userRepo;
    }

    public List<GroupDTO> getAllGroups() {
        List<GroupDTO> groupDTOList = new ArrayList<>();
        groupDTOList = groupRepo.findAll().stream().map(this::convertToDTO
        ).collect(Collectors.toList());
        return groupDTOList;
    }

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group newGroup = new Group(groupDTO.getName());

        Group finalNewGroup = newGroup;
        groupDTO.getUsers().forEach(username -> {
            if (userRepo.findByUsername(username).isPresent()) {
                finalNewGroup.addUser(userRepo.findByUsername(username).get());
            }
        });

        newGroup = groupRepo.save(finalNewGroup);
        return convertToDTO(groupRepo.getById(newGroup.getId()));
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(group.getId());
        groupDTO.setName(group.getName());
        Set<String> usernames = new HashSet<String>();
        group.getUserList().forEach(user -> {
            usernames.add(user.getUsername());
        });

        groupDTO.setUsers(usernames);
        return groupDTO;
    }

    public void deleteGroup(long groupId) {
        groupRepo.deleteById(groupId);
    }

    public GroupDTO getGroupById(long groupId) {
        return convertToDTO(groupRepo.findById(groupId).orElseThrow(
                () -> new EntityNotFoundException("Group not not Found")));

    }

    public GroupDTO getGroupByName(String groupName) {
        return convertToDTO(groupRepo.findByName(groupName).orElseThrow(
                () -> new EntityNotFoundException("Group not not Found")));

    }

    public List<String> getUsers(long groupId) {
        Group group = groupRepo.findById(groupId).get();
        return group.getUserList().stream().map(User::getUsername).collect(Collectors.toList());
    }

    public GroupDTO addUser(long groupId, String username) {
        User userToAdd = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not not Found"));

        group.addUser(userToAdd);
        return convertToDTO(groupRepo.save(group));
    }

    public GroupDTO removeUser(long groupId, String username) {
        User userToRemove = userRepo.findByUsername(username).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new EntityNotFoundException("Group not not Found"));
        group.removeUser(userToRemove);
        return convertToDTO(groupRepo.save(group));
    }

}
