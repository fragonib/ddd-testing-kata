package com.lookiero.qdevtest.goodtime2go.service;

import com.lookiero.qdevtest.goodtime2go.model.entity.WeatherReport;

public interface WeatherReportService {

  public WeatherReport getReport(final String location);
}
