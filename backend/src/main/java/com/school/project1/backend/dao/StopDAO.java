package com.school.project1.backend.dao;

import com.school.project1.backend.model.Stop;

import tools.jackson.databind.json.JsonMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;
import org.springframework.core.io.Resource;

@Repository
public class StopDAO {

  private final JsonMapper mapper;
  private final Resource bestand;

  private record StopNavigation(List<Stop> stops) {
  }

  public StopDAO(JsonMapper mapper, @Value("classpath:stop-navigation.json") Resource bestand) {
    this.mapper = mapper;
    this.bestand = bestand;
  }

  public List<Stop> findAllStops() {
    try (
        InputStream invoer = bestand.getInputStream()) {
      StopNavigation stopNavigation = mapper.readValue(invoer, StopNavigation.class);
      return stopNavigation.stops();
    } catch (IOException e) {
      throw new UncheckedIOException("foutmelding voor inlezen bestand", e);
    }
  }
}
