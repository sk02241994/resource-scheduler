package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.altres.connection.util.PatternChecker;
import com.altres.rs.constants.Constants;
import com.altres.rs.dao.LoginDao;

/**
 * Servlet for change password jsp will validate if new user and make user change the password if user is logging in 
 * for first time.
 */
@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = Logger.getLogger(ChangePasswordServlet.class.getName());
  
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

    PatternChecker checker = new PatternChecker();
    
    RequestDispatcher dispatcher = null;
    String errorMessage = "Password must contain at least one letter, at least one number";

    String emailAddress = (String) request.getSession(false).getAttribute("login_servlet_email");
    String newpassword = request.getParameter("old_password");
    String confirmPassword = request.getParameter("new_password");

    if (!newpassword.equals(confirmPassword)) {
      request.setAttribute("errorInPasswordMatching", "Password does not match. Please try again");
      dispatcher = request.getRequestDispatcher(Constants.CHANGE_PASSWORD_JSP);
      dispatcher.forward(request, response);
      return;
    }

    if (!checker.validatePassword(newpassword)) {
      request.setAttribute("errorInPatternMatching", errorMessage);
      dispatcher = request.getRequestDispatcher(Constants.CHANGE_PASSWORD_JSP);
      dispatcher.forward(request, response);
      return;
    }

    LoginDao logindao = new LoginDao();

    try {
      logindao.changePassword(emailAddress, newpassword);
      request.setAttribute("success_message", "Password changed successfully please login again");
      dispatcher = request.getRequestDispatcher(Constants.CHANGE_PASSWORD_JSP);
      dispatcher.forward(request, response);
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while changing password", exception);
      throw new ServletException("There was a problem while trying to change the password.");
    }

  }
}
