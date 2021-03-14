package com.altres.rs.model;

/**
 * Class that contains getter and setter for user details.
 */
public class User {
  
  private int rsUserId;
  private String firstname;
  private String lastname;
  private String email_address;
  private String password;
  private String designation;
  private String address;
  private String department;
  private boolean isEnabled;
  private boolean isAdmin;
  
  public String getFirstname() {
    return firstname;
  }
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }
  public String getLastname() {
    return lastname;
  }
  public void setLastname(String lastname) {
    this.lastname = lastname;
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
  public String getDesignation() {
    return designation;
  }
  public void setDesignation(String designation) {
    this.designation = designation;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getDepartment() {
    return department;
  }
  public void setDepartment(String department) {
    this.department = department;
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
