package com.altres.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ResourceSchedulerServlet<T> extends HttpServlet implements ResourceInterface<T> {

  private static final long serialVersionUID = 1L;
  
  private HttpServletRequest request;
  private HttpServletResponse response;

  public void setRequestResponse(HttpServletRequest request, HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }
  
  public void displayModalNotice() {
    displayModalNotice(request);
  }
  
  public void displayNotice() {
    displayNotice(request);
  }

  public void isAdmin() throws ServletException {
    isAdmin(request);
  }
  
  public void forward(String jsp) throws ServletException, IOException {
    forward(request, response, jsp);
  }
  
  public <E> void setAttribute(String attributeKey, E variable) {
    setAttribute(request, attributeKey, variable);
  }
  
  public String getParameter(String key) {
    return getParameter(request, key);
  }

  public Object getSessionAttribute(String key) {
    return getSessionAttribute(request, key);
  }
}
