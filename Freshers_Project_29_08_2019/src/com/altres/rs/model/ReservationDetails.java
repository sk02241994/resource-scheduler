package com.altres.rs.model;

/**
 * Class that contain getter and setter method for reservation details.
 */
public class ReservationDetails extends Reservation{
  
  private String userName;
  private String resourceName;
  
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }
  public String getResourceName() {
    return resourceName;
  }
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }
}