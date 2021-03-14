package com.altres.rs.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for error page. Will display error page when exception is thrown by other servlets.
 */
@WebServlet("/ErrorServlet")
public class ErrorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ErrorServlet() {
        super();
    }

  /* 
   * Will handle exceptions thrown by do get methods of other servlet.
   * 
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  handleExceptions(request, response);	  
	}

  /* 
   * Will handle exception thrown by do post methods of other servlets.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    handleExceptions(request, response);
  }
  
  /**
   * Does the job of catching the exceptions thrown and redirecting to error page along with what the message was when
   * the exception is thrown.
   * 
   * @param request
   * @param response
   * @throws ServletException
   */
  private void handleExceptions(HttpServletRequest request, HttpServletResponse response) throws ServletException {
  
    Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
    String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    
    if(servletName == null)
      servletName = "Unknown";
    
    String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
    
    if(requestUri == null)
      requestUri = "Unknown";
    
    request.setAttribute("status_code", statusCode);
    request.setAttribute("request_uri", requestUri);
    request.setAttribute("servlet_name", servletName);
    request.setAttribute("exception_name", throwable.getClass().getName());
    request.setAttribute("exception_message", throwable.getMessage());
    RequestDispatcher dispatcher = request.getRequestDispatcher("rs-jsp/error.jsp");
    try {
      dispatcher.forward(request, response);
    } catch (ServletException | IOException e) {
        throw new ServletException("Error in error page");
    }
  }

}
