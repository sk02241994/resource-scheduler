package com.altres.connection.util;

import com.altres.rs.model.User;

/**
 * Class for validating user details of user being a valid user or not
 */
public class ValidateUser {

  private User user;

  public void setUser(User user) {
    this.user = user;
  }

  public boolean isValidLogin(String emailAddress, String password) {
    return emailAddress.equals(user.getEmail_address()) && password.equals(user.getPassword());
  }

  public boolean isAdmin() {
    return user.isAdmin();
  }

  public boolean isEnabled() {
    return user.isEnabled();
  }

  public boolean isFirstLogin(String password) {
    return "user".equals(password);
  }

}
