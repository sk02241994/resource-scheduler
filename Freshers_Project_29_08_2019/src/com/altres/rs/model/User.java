package com.altres.rs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.altres.utils.PojoSavable;
import com.altres.utils.exception.ValidationServletException;

/**
 * Class that contains getter and setter for user details.
 */
public class User implements Serializable, PojoSavable<Void> {

  private static final long serialVersionUID = 1L;

  private Integer rsUserId;
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

  public Integer getRsUserId() {
    return rsUserId;
  }

  public void setRsUserId(Integer rsUserId) {
    this.rsUserId = rsUserId;
  }

  public void setIsAdmin(boolean isAdmin) {
    this.isAdmin = isAdmin;
  }

  public boolean isAdmin() {
    return this.isAdmin;
  }

  @Override
  public void sanitize() {
    setEmail_address(StringUtils.trimToNull(getEmail_address()));
    setName(StringUtils.trimToNull(getName()));
  }

  @Override
  public void validate(Void variable) throws ValidationServletException {

    List<String> error = new ArrayList<>();

    if (StringUtils.isBlank(getName())) {
      error.add("Please enter name.");
    }

    if (StringUtils.isNotBlank(getName()) && !Pattern.matches("^[a-zA-Z]{1,60}\\s?[a-zA-Z]{1,30}$", getName())) {
      error.add("Please enter valid name.");
    }

    if (StringUtils.isBlank(getEmail_address())) {
      error.add("Please enter email.");
    }

    if (StringUtils.isNotBlank(getEmail_address())
        && !Pattern.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$", getEmail_address())) {
      error.add("Please enter valid email.");
    }

    if (!error.isEmpty()) {
      throw new ValidationServletException(error);
    }

  }
}
