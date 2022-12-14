package com.reunet.app.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.Activity;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.services.ActivityServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityController {

    @Autowired
    ActivityServices activityServices;

    @GetMapping("")
    public ResponseEntity<Response<List<Activity>>> getAllActivitysByGroupId(@RequestParam Long groupid) {
        try {
            List<Activity> groups = activityServices.findAllByGroupsId(groupid);
            return ResponseEntity.ok().body(new Response<List<Activity>>(
                    HttpServletResponse.SC_OK,
                    "",
                    groups));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<List<Activity>>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Activity>> activityById(@PathVariable("id") Long id) {
        try {
            Activity activitie = activityServices.findActivityById(id).orElseThrow();

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    activitie));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<Activity>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @PostMapping("")
    public ResponseEntity<Response<Activity>> create(@RequestBody Activity activity) {
        try {

            Activity createActivity = activityServices.createActivity(activity);

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    createActivity));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<Activity>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<Activity>> update(@PathVariable("id") String id, @RequestBody Activity activity) {
        try {
            activity.setId(Long.parseLong(id));

            Activity createActivity = activityServices.updateActivity(activity);

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    createActivity));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<Activity>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<String>> delete(@PathVariable("id") Long id) {
        try {
            activityServices.deleteByActivityId(id);
            return ResponseEntity.ok().body(new Response<String>(
                    HttpServletResponse.SC_OK,
                    "",
                    null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

}
