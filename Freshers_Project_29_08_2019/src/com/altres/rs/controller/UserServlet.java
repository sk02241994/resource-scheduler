package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.altres.rs.constants.Constants;
import com.altres.rs.dao.UserDao;
import com.altres.rs.model.User;
import com.altres.utils.Gender;
import com.altres.utils.ResourceSchedulerServlet;
import com.altres.utils.exception.ValidationServletException;
import com.google.gson.Gson;

/**
 * Servlet for adding, updating and modifying new user.
 */
@WebServlet("/UserServlet")
public class UserServlet extends ResourceSchedulerServlet<User> {
  private static final String USER_ID = "userId";
  private static final String USER = "user";
  private static final String USERS = "users";
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());

  public UserServlet() {
    super();
  }

  /*
   * This will allow administrator to get detailed information on particular user and edit the user information
   * including designation, department, address and name.
   * 
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    setRequestResponse(request, response);
    isAdmin();
    clearNotices();

    UserDao userDao = new UserDao();
    String userId = getParameter(USER_ID);

    try {
      if (StringUtils.isNotBlank(userId)) {
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(userDao.getSingleUser(userId)));
        return;
      }

      setAttribute(USERS, userDao.getUser());

    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while trying to get users", exception);
      throw new ServletException("There was an error while trying to get the user details");
    }
    displayNotice();
    forward(Constants.MANAGE_USER_JSP);
  }

  /*
   * Will be able to add new user data to database or edit the existing user information.
   * 
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    setRequestResponse(request, response);
    isAdmin();
    clearNotices();

    UserDao userDao = new UserDao();

    User user = getForm(request);

    try {

      user.sanitizeAndValidate();
      user.setRsUserId(userDao.upsert(user));
      addSuccessNotice("User details have been saved.");
    } catch (ValidationServletException e) {
      Gson gson = new Gson();
      setAttribute(USER, gson.toJson(user));
      addModalErrorNotice(e.getError());
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while trying to get a single user", exception);
      throw new ServletException("There was an error while trying to save user credentials");
    }

    try {
      setAttribute(USERS, userDao.getUser());
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Exception while trying to get a single user", e);
      throw new ServletException("There was an error while trying to save user credentials");
    }
    displayNotice();
    forward(Constants.MANAGE_USER_JSP);
  }

  @Override
  public User getForm(HttpServletRequest request) {

    String firstname = getParameter("name");
    String emailAddress = getParameter("email");
    boolean isEnabled = "on".equals(getParameter("isenabled"));
    boolean isAdmin = "on".equals(getParameter("isadmin"));
    Integer userId = NumberUtils.isCreatable(getParameter(USER_ID))
        ? NumberUtils.toInt(getParameter(USER_ID))
        : null;
    String genderSelected = getParameter("gender");
    Gender gender = StringUtils.isNotBlank(genderSelected) ? Gender.valueOf(getParameter("gender")) : null;
    boolean isPermanetEmployee = "on".equals(getParameter("isPermanentEmployee"));

    User user = new User();

    user.setName(firstname);
    user.setEmail_address(emailAddress);
    user.setEnabled(isEnabled);
    user.setIsAdmin(isAdmin);
    user.setRsUserId(userId);
    user.setGender(gender);
    user.setIsPermanentEmployee(isPermanetEmployee);

    return user;
  }
}
