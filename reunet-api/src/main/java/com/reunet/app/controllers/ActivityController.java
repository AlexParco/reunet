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
import org.springframework.web.bind.annotation.RestController;

import com.reunet.app.models.Activity;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.services.ActivityServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/activity")
public class ActivityController {

    @Autowired
    private ActivityServices activityServices;

    @GetMapping("")
    private ResponseEntity<?> allActivities() {
        try {
            List<Activity> activities = activityServices.findAllActivities();

            return ResponseEntity.ok().body(new Response<List<Activity>>(
                    HttpServletResponse.SC_OK,
                    "",
                    activities));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> activityById(@PathVariable("id") Long id) {
        try {
            Activity activitie = activityServices.findActivityById(id).orElseThrow();

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    activitie));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @PostMapping("")
    private ResponseEntity<?> create(@RequestBody Activity activity) {
        try {

            Activity createActivity = activityServices.createActivity(activity);

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    createActivity));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @PutMapping("{id}")
    private ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Activity activity) {
        try {
            activity.setId(Long.parseLong(id));

            Activity createActivity = activityServices.updateActivity(activity);

            return ResponseEntity.ok().body(new Response<Activity>(
                    HttpServletResponse.SC_OK,
                    "",
                    createActivity));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @DeleteMapping("{id}")
    private ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            activityServices.deleteActivity(Long.parseLong(id));
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
