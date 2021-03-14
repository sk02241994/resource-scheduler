package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import com.altres.rs.constants.Constants;
import com.altres.rs.dao.UserDao;
import com.altres.rs.model.User;

/**
 * Servlet for adding, updating and modifying new user.
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(UserServlet.class.getName());
  
  public UserServlet() {
    super();
  }

 /* 
  * This will allow administrator to get detailed information on particular user and edit the user information including
  * designation, department, address and name.
  * 
 */

@Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    UserDao userDao = new UserDao();
    String formAction = request.getParameter("form_action");

    try {
    if ("edit".equals(formAction)) {

      String emailAddress = request.getParameter("email_address");

        request.setAttribute("singleUser", userDao.getSingleUser(emailAddress));

    }

      request.setAttribute("user", userDao.getUser());
      
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while trying to get users", exception);
      throw new ServletException("There was an error while trying to get the user details");
    }
    RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.MANAGE_USER_JSP);
    dispatcher.forward(request, response);
  }
  
  /* 
   * Will be able to add new user data to database or edit the existing user information.
   * 
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    UserDao userDao = new UserDao();

    
    String editUserField = request.getParameter("edit");
    
    String firstname = request.getParameter("first_name");
    String lastname = request.getParameter("last_name");
    String emailAddress = request.getParameter("email");
    String designation = request.getParameter("designation");
    String address = request.getParameter("address");
    String department = request.getParameter("department");
    boolean isEnabled = "on".equals(request.getParameter("isenabled"));
    boolean isAdmin = "on".equals(request.getParameter("isadmin"));

    User user = new User();

    if(firstname.equals("") || lastname.equals("") || emailAddress.equals("") || designation == null || 
        department == null) {
      request.setAttribute("error_message", "All user details must be filled");
    }
    else {

      user.setFirstname(firstname);
      user.setLastname(lastname);
      user.setEmail_address(emailAddress);
      user.setDesignation(designation);
      user.setAddress(address);
      user.setDepartment(department);
      user.setEnabled(isEnabled);
      user.setIsAdmin(isAdmin);

      try {
        if ("edit_user".equals(editUserField)) {
          userDao.updateUser(user);
          request.setAttribute("success_message", "User details have been updated");
        } else {
          UserDao userdao = new UserDao();
          userdao.saveUser(user);
          request.setAttribute("success_message", "User details have been added");
        }
      } catch (SQLException exception) {
        LOGGER.log(Level.SEVERE, "Exception while trying to get a single user", exception);
        throw new ServletException("There was an error while trying to save user credentials");
      }
    }
    try {
      request.setAttribute("user", userDao.getUser());
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while trying to get users", exception);
      throw new ServletException("There was an error while trying to get the user details");
    }
    RequestDispatcher dispatcher = request.getRequestDispatcher(Constants.MANAGE_USER_JSP);
    dispatcher.forward(request, response);
  }
}
