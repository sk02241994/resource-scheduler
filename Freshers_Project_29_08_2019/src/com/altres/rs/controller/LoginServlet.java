package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.altres.connection.util.ValidateUser;
import com.altres.rs.constants.Constants;
import com.altres.rs.dao.LoginDao;
import com.altres.rs.model.User;

/**
 * Class for handling all the login verifications and passing the user or administrator to their respective pages
 * also validating the user if valid and active or inactive user.
 */
public class LoginServlet extends HttpServlet {
  public static final String LOGGEDIN_USER = "loggedInUser";

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
  /*
   * Here the session will be invalidated when the user presses logout button allowing the user to logout and go to
   * login page again.
   * 
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String logoutCommand = request.getParameter("action");
    HttpSession session = request.getSession(false);
    
    if ("logout".equals(logoutCommand) && session != null) {
      session.removeAttribute(LOGGEDIN_USER);
      session.invalidate();
      response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    }
    request.getRequestDispatcher(Constants.LOGIN_JSP).include(request, response);
  }

  /*
   * In this the user after entering credentials that is email address and password; the user will be sent to appropriate
   * page.
   * 
   * If the user is logging in for first time he will be sent to change password page, if the user is disabled he wont
   * be able to login and if he is a administrator he will be sent to administrator page as for user he will be sent to user page.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    LoginDao logindao = new LoginDao();
    String emailAddress = request.getParameter("uemail_address");
    String password = request.getParameter("user_password");
    User user = null;

    
    try {
      user = logindao.getUser(emailAddress, password);
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while logging in as a valid user", exception);
      throw new ServletException("There was an error while trying to get your credentials");
    }
    validateAndForward(emailAddress, password, request, response, user);

  }

  /**
   * Method for validating and forwarding the user or administrator to their respective pages will give message
   * if the user is valid or not and if the user is active or inactive user.
   * 
   * @param emailAddress
   * @param password
   * @param request
   * @param response
   * @param user
   * @throws ServletException
   */
  private void validateAndForward(String emailAddress, String password, HttpServletRequest request,
      HttpServletResponse response, User user) throws ServletException {

    ValidateUser validateUser = new ValidateUser();  
    validateUser.setUser(user);
    String contextPath = getServletContext().getContextPath();
    String redirectUrl = "";
    boolean hasError = false;
    
    try {
      if (!validateUser.isValidLogin(emailAddress, password)) {
        request.setAttribute("errorMessage", "Invalid email or password");
        hasError = true;
      } else if (validateUser.isAdmin()) {
        redirectUrl = "/UserServlet";
      } else if (validateUser.isEnabled()) {

        if (validateUser.isFirstLogin(password)) {
          redirectUrl = "/ChangePasswordServlet";
        } else {
          redirectUrl = "/ReservationServlet";
        }
      } else {
        request.setAttribute("disableError", "Account disabled, please contact administrator.");
        hasError = true;
      }
      
      if(!hasError) {
        HttpSession session = request.getSession();
        session.setAttribute(LOGGEDIN_USER, user);
        response.sendRedirect(contextPath + redirectUrl);
      } else {
        request.getRequestDispatcher(Constants.LOGIN_JSP).forward(request, response);
      }
    } catch (IOException | ServletException exception) {
      LOGGER.log(Level.SEVERE, "Exception while logging in as a valid user", exception);
      throw new ServletException("There was an error while logging in");
    }
  }
}
