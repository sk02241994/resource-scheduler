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

import com.altres.rs.constants.Constants;
import com.altres.rs.dao.ResourceDao;
import com.altres.rs.model.Resource;

/**
 * Servlet for adding, updating and deleting resources.
 */
@WebServlet("/ResourceServlet")
public class ResourceServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(ResourceServlet.class.getName());
  private static final String SUCCESS_MESSAGE = "success_message";
  
  public ResourceServlet() {
    super();
  }

  /* 
   * Tthe administrator will be shown the available resources from database active and inactive, 
   * the administrator will be able to add new resources delete the resource and change the status of the resources.
   * 
   * When request is made for update or delete id will be passed and the required actions will be performed.
   *  
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    String parameter = "resource_id";

    ResourceDao resourceDao = new ResourceDao();
    String formAction = request.getParameter("form_action");
    
    RequestDispatcher dispatcher = null;

    try {
      switch (formAction == null ? "" : formAction) {
      case "edit":
        int editId = Integer.parseInt(request.getParameter(parameter));
        request.setAttribute("singleResource", resourceDao.getSingleResource(editId));
        request.setAttribute("resources", resourceDao.getResource());
        break;

      case "delete":
        int deleteId = Integer.parseInt(request.getParameter(parameter));
        resourceDao.deleteResource(deleteId);
        request.setAttribute(SUCCESS_MESSAGE, "Resource has been deleted successfully");
        request.setAttribute("resources", resourceDao.getResource());
        break;

      case "":
        request.setAttribute("resources", resourceDao.getResource());
        break;

      default:
        break;
      }
      
    }catch (SQLException exception) {
      request.setAttribute("resourcesDeleteError", "The resources cannot be deleted because they are already in use");
      LOGGER.log(Level.SEVERE, "Exception while getting all the resources", exception);
      try {
        request.setAttribute("resources", resourceDao.getResource());
      } catch (SQLException e) {
        throw new ServletException("Unable to retrieve data");
      }
    }
    dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESOURCE_JSP);
    dispatcher.forward(request, response);

  }

  /* 
   * When request is made for edit operation, the operation to edit will be called. 
   * When the  request is made to add resource, the operation to add will be called.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    RequestDispatcher dispatcher = null;
    ResourceDao resourceDao = new ResourceDao();
    String editResourceField = request.getParameter("edit_resource");

    String resourcename = request.getParameter("resource_name");
    String description = request.getParameter("description");
    boolean isEnabled = "on".equals(request.getParameter("isenabled"));

    Resource resource = new Resource();

    try {
      if (resourcename.equals("")) {
        request.setAttribute("error_message", "Resource name cannot be empty");
        request.setAttribute("resources", resourceDao.getResource());
        dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESOURCE_JSP);
      } else {

        resource.setResourceName(resourcename);
        resource.setResourceDescription(description);
        resource.setEnabled(isEnabled);

        if ("edit_resource".equals(editResourceField)) {
          int id = Integer.parseInt(request.getParameter("resource_id"));
          resource.setRsResourceId(id);
          resourceDao.updateResource(resource);
          request.setAttribute(SUCCESS_MESSAGE, "Resource has been updated successfully");
          dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESOURCE_JSP);

        } else {

          resourceDao.saveResource(resource);
          request.setAttribute(SUCCESS_MESSAGE, "Resource has been saved successfully");
          dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESOURCE_JSP);
        }

      }
      request.setAttribute("resources", resourceDao.getResource());
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while posting all the details of resources", exception);
      throw new ServletException("Error while trying to post details");
    }

    dispatcher.forward(request, response);
  }
}