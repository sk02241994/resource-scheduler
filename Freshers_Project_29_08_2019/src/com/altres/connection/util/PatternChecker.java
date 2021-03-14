package com.altres.connection.util;

import java.util.regex.Pattern;

/**
 * Class is for validation of patterns of email and password
 */
public class PatternChecker {
  
  
  /**
   * Method is for pattern validation
   * 
   * @param emailAddress
   * @return
   */
  public boolean validateEmail(String emailAddress) {
  
    String regex = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";     
    return Pattern.compile(regex).matcher(emailAddress).matches();
  }
  
  /**
   * Method is for password validation
   * 
   * @param password
   * @return
   */
  public boolean validatePassword(String password) {
    String regex = "^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$";
    return Pattern.compile(regex).matcher(password).matches();
  }
  
}
