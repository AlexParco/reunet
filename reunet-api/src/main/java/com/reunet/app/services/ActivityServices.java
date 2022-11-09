package com.reunet.app.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reunet.app.models.Activity;
import com.reunet.app.models.Group;
import com.reunet.app.repository.ActivityReponsitory;

@Service
public class ActivityServices {

    @Autowired
    private ActivityReponsitory activityReponsitory;

    public List<Activity> findAllActivities() {
        return activityReponsitory.findAll();
    }

    public Optional<Activity> findActivityById(Long activityId) {
        return activityReponsitory.findById(activityId);
    }

    public void deleteActivity(Long id) {
        activityReponsitory.deleteById(id);
    }

    public Activity createActivity(Activity activity) {
        activity.setCreatedAt(new Date());
        activity.setUpdatedAt(new Date());
        return activityReponsitory.save(activity);
    }

    public Activity updateActivity(Activity activity) {
        activity.setCreatedAt(new Date());
        activity.setUpdatedAt(new Date());
        return activityReponsitory.save(activity);
    }

    public List<Group> findAllByGroupsId(Long groupId) {
        return activityReponsitory.findAllByGroupId(groupId);
    }

}
