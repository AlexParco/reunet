package com.reunet.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reunet.app.models.Activity;

@Repository
public interface ActivityReponsitory extends JpaRepository<Activity, Long> {
}
