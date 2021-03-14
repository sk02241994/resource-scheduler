package com.altres.rs.model;

/**
 * Class that contains getter and setter for reservation details.
 */
public class Reservation {
  
  private int reservationId;
  private int userId;
  private int resourceId;
  private String startDate;
  private String startTime;
  private String endDate;
  private String endTime;
  
  public int getUserId() {
    return userId;
  }
  public void setUserId(int userId) {
    this.userId = userId;
  }
  public int getResourceId() {
    return resourceId;
  }
  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }
  public String getStartDate() {
    return startDate;
  }
  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }
  public String getStartTime() {
    return startTime;
  }
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  public String getEndDate() {
    return endDate;
  }
  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
  public String getEndTime() {
    return endTime;
  }
  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
  public int getReservationId() {
    return reservationId;
  }
  public void setReservationId(int reservationId) {
    this.reservationId = reservationId;
  }
  
}
