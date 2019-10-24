package com.lookiero.qdevtest.goodtime2go.model.entity;

public class WeatherReport {

  private String location;
  private boolean checkOut;

  public WeatherReport(final String location, final boolean checkOut) {
    this.location = location;
    this.checkOut = checkOut;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public boolean isCheckOut() {
    return checkOut;
  }

  public void setCheckOut(final boolean checkOut) {
    this.checkOut = checkOut;
  }
}
