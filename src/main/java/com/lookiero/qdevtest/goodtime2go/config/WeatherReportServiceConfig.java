package com.lookiero.qdevtest.goodtime2go.config;

import com.lookiero.qdevtest.goodtime2go.service.WeatherReportService;
import com.lookiero.qdevtest.goodtime2go.service.WeatherReportServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeatherReportServiceConfig {

  @Bean
  public WeatherReportService getTimeReportService() {
    return new WeatherReportServiceImpl();
  }
}
