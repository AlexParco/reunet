package com.reunet.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reunet.app.models.Activity;
import com.reunet.app.models.Group;

@Repository
public interface ActivityReponsitory extends JpaRepository<Activity, Long> {

    List<Group> findAllByGroupId(Long group_id);
}
