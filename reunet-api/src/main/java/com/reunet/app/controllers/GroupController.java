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

import com.reunet.app.models.Group;
import com.reunet.app.models.payload.response.Response;
import com.reunet.app.services.GroupServices;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/group")
public class GroupController {

    @Autowired
    private GroupServices groupServices;

    @GetMapping("")
    private ResponseEntity<?> allGroups() {
        try {
            List<Group> groups = groupServices.findAllGroups();
            return ResponseEntity.ok().body(new Response<List<Group>>(
                    HttpServletResponse.SC_OK,
                    "",
                    groups));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ""));
        }
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> groupById(@PathVariable("id") String id) {
        try {
            Group group = groupServices.findGroupById(Long.parseLong(id)).orElseThrow();
            return ResponseEntity.ok().body(new Response<Group>(
                    HttpServletResponse.SC_OK,
                    "",
                    group));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ""));
        }
    }

    @PostMapping("")
    private ResponseEntity<?> create(@RequestBody Group group) {
        try {

            Group createGroup = groupServices.createGroup(group);

            return ResponseEntity.ok().body(new Response<Group>(
                    HttpServletResponse.SC_OK,
                    "",
                    createGroup));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<String>(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    null));
        }
    }

    @PutMapping("{id}")
    private ResponseEntity<?> update(@PathVariable("id") String id, @RequestBody Group group) {
        try {
            group.setId(Long.parseLong(id));
            Group updateGroup = groupServices.updateGroup(group);

            return ResponseEntity.ok().body(new Response<Group>(
                    HttpServletResponse.SC_OK,
                    "",
                    updateGroup));

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
            groupServices.deleteGroup(Long.parseLong(id));
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
