package com.reunet.app.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.Group;
import com.reunet.app.repository.GroupRepository;

@Service
public class GroupServices {

    @Autowired
    private GroupRepository groupRepository;

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findGroupById(Long id) {
        return groupRepository.findById(id);
    }

    public Group createGroup(Group group) {
        group.setCreatedAt(new Date());
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    public Group updateGroup(Group group) {
        group.setCreatedAt(new Date());
        group.setUpdatedAt(new Date());
        return groupRepository.save(group);
    }

    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }

}
