package com.altres.rs.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.altres.rs.constants.Constants;
import com.altres.rs.controller.LoginServlet;
import com.altres.rs.model.User;


/**
 * Filter to check if the user has logged out and if logged out, user will have to login again to use services.
 */
@WebFilter("/SessionFilter")
public class SessionFilter implements Filter {

  public SessionFilter() {
  }

  public void destroy() {
  }

  /* 
   * This method will check if the user has logged out by checking the session and if logged out the user will be sent
   * to login page if user tries to use services after logging out.
   * 
   * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest httpServletRequest = (HttpServletRequest) request;
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    RequestDispatcher dispatcher = null;

    HttpSession session = httpServletRequest.getSession(false);
    
    User user = null;
        if(session != null) {
          user = (User) session.getAttribute(LoginServlet.LOGGEDIN_USER);
        } 
        

    if (httpServletRequest.getServletPath().endsWith(".css") || httpServletRequest.getServletPath().endsWith(".js")
        || httpServletRequest.getServletPath().endsWith(".png")) {
      chain.doFilter(request, response);
    } else {
      if (user != null && (user.getEmail_address() != null)
          || (httpServletRequest.getServletPath().endsWith("/LoginServlet"))) {
        chain.doFilter(request, response);
      } else {
        dispatcher = httpServletRequest.getRequestDispatcher(Constants.LOGIN_JSP);
        dispatcher.forward(httpServletRequest, httpServletResponse);
      }
    }

  }

  public void init(FilterConfig fConfig) throws ServletException {
  }

}
