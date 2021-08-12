package com.altres.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestResponseHandler {

  default void forward(HttpServletRequest request, HttpServletResponse response, String jsp)
      throws ServletException, IOException {
    request.getRequestDispatcher(jsp).forward(request, response);
  }

  default <T> void setAttribute(HttpServletRequest request, String attributeKey, T variable) {
    request.setAttribute(attributeKey, variable);
  }
  
  default String getParameter(HttpServletRequest request, String key) {
    return request.getParameter(key);
  }

  default Object getSessionAttribute(HttpServletRequest request, String key) {
    return request.getSession().getAttribute(key);
  }
}
