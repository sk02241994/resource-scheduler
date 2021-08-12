package com.altres.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.altres.rs.controller.LoginServlet;
import com.altres.rs.model.User;

public interface ValidateIsAdmin {

  default void isAdmin(HttpServletRequest request) throws ServletException {
    HttpSession session = request.getSession(false);
    User user = (User)session.getAttribute(LoginServlet.LOGGEDIN_USER);
    
    if(!user.isAdmin()) {
      throw new ServletException("You do not have permission to access these resources.");
    }
  }
}
