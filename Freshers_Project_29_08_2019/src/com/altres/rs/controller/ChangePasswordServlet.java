package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.altres.rs.constants.Constants;
import com.altres.rs.dao.LoginDao;
import com.altres.rs.model.User;
import com.altres.utils.ResourceSchedulerServlet;

/**
 * Servlet for change password jsp will validate if new user and make user change the password if user is logging in 
 * for first time.
 */
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends ResourceSchedulerServlet<User> {
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    setRequestResponse(request, response);
    clearNotices();
    forward(Constants.CHANGE_PASSWORD_JSP);
  }
  
  /*
   * in this servlet after login this page is displayed for first time user and once changed the user will again be
   * redirected to login page for security.
   * 
   * Validation is done here to check if the password entered has one number and alphabet and is of size 6 and greater.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    setRequestResponse(request, response);
    clearNotices();

    boolean isValid = true;

    User user = getForm(request);
    String emailAddress = null;
    if (user != null) {
      emailAddress = user.getEmail_address();
    } else {
      throw new ServletException("There was a problem while changing password");
    }

    String newpassword = getParameter("old_password");
    String confirmPassword = getParameter("new_password");

    if (!StringUtils.equals(newpassword, confirmPassword)) {
      addModalErrorNotice("Password does not match. Please try again");
      isValid = false;
    }

    if (!Pattern.matches("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{6,}$", newpassword)) {
      addModalErrorNotice("Password must contain at least one letter, at least one number");
      isValid = false;
    }

    if(isValid) {
      try {
        LoginDao logindao = new LoginDao();
        logindao.changePassword(emailAddress, newpassword);
        addSuccessNotice("Password changed successfully please login again");
      } catch (SQLException exception) {
        LOGGER.log(Level.SEVERE, "Exception while changing password", exception);
        throw new ServletException("There was a problem while trying to change the password.");
      }
    }

    displayNotice();
    forward(Constants.CHANGE_PASSWORD_JSP);
  }

  @Override
  public User getForm(HttpServletRequest request) {
    return ((User) request.getSession(false).getAttribute(LoginServlet.LOGGEDIN_USER));
  }
}
