package com.lookiero.qdevtest.goodtime2go.controller;

import com.lookiero.qdevtest.goodtime2go.service.WeatherReportService;
import com.lookiero.qdevtest.goodtime2go.model.entity.WeatherReport;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/getReport/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class WeatherReportController {

  private WeatherReportService weatherReportService;

  public WeatherReportController(final WeatherReportService weatherReportService) {
    this.weatherReportService = weatherReportService;
  }

  @GetMapping(value = "/{location}")
  public ResponseEntity<WeatherReport> getReport(@PathVariable final String location) {
    final WeatherReport report = weatherReportService.getReport(location);
    return ResponseEntity.ok(report);
  }

}
