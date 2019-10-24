package com.lookiero.qdevtest.goodtime2go.service;

import com.lookiero.qdevtest.goodtime2go.model.entity.WeatherReport;

public class WeatherReportServiceImpl implements WeatherReportService {

  @Override
  public WeatherReport getReport(final String location) {
    final WeatherReport report = new WeatherReport(location, true);
    return report;
  }
}
