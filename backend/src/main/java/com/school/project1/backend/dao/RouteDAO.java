package com.school.project1.backend.dao;

import com.school.project1.backend.model.Stop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RouteDAO {

  public List<Stop> findAllStops() {
    return List.of();
  }
}
