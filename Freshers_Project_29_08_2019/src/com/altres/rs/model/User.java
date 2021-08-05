package com.altres.rs.model;

/**
 * Class that contains getter and setter for user details.
 */
public class User {
  
  private int rsUserId;
  private String name;
  private String email_address;
  private String password;
  private boolean isEnabled;
  private boolean isAdmin;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public String getEmail_address() {
    return email_address;
  }
  public void setEmail_address(String email_address) {
    this.email_address = email_address;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isEnabled() {
    return isEnabled;
  }
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  public int getRsUserId() {
    return rsUserId;
  }
  public void setRsUserId(int rsUserId) {
    this.rsUserId = rsUserId;
  }
  
  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public boolean isAdmin() {
    return this.isAdmin;
  }
}
