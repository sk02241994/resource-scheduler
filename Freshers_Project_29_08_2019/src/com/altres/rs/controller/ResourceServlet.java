package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.altres.rs.constants.Constants;
import com.altres.rs.dao.ResourceDao;
import com.altres.rs.model.Resource;
import com.altres.utils.ResourceSchedulerServlet;
import com.altres.utils.exception.ValidationServletException;
import com.google.gson.Gson;

/**
 * Servlet for adding, updating and deleting resources.
 */
@WebServlet("/ResourceServlet")
public class ResourceServlet extends ResourceSchedulerServlet<Resource> {
  private static final String FORM = "form";
  private static final String RESOURCE_ID = "resource_id";
  private static final String RESOURCES = "resources";
  private static final long serialVersionUID = 1L;
  private static final Logger LOGGER = Logger.getLogger(ResourceServlet.class.getName());

  public ResourceServlet() {
    super();
  }

  /*
   * Tthe administrator will be shown the available resources from database active and inactive, the administrator will
   * be able to add new resources delete the resource and change the status of the resources.
   * 
   * When request is made for update or delete id will be passed and the required actions will be performed.
   * 
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    setRequestResponse(request, response);
    isAdmin();
    clearNotices();

    String parameter = "resourceId";

    ResourceDao resourceDao = new ResourceDao();
    String formAction = getParameter("form_action");
    List<Resource> resources = new ArrayList<>();

    try {
      resources.addAll(resourceDao.getResource());

      switch (StringUtils.defaultIfBlank(formAction, "")) {
      case "edit":
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(resourceDao.getSingleResource(NumberUtils.toInt(getParameter(parameter)))));
        return;

      case "delete":
        new Resource().validateDelete(NumberUtils.toInt(getParameter(parameter)));
        resourceDao.deleteResource(NumberUtils.toInt(getParameter(parameter)));
        addSuccessNotice("Resource has been deleted successfully");
        break;

      default:
        break;
      }

    } catch (ValidationServletException e) {
      addErrorNotice(e.getError());
    } catch (SQLException exception) {
      request.setAttribute("resourcesDeleteError", "The resources cannot be deleted because they are already in use");
      LOGGER.log(Level.SEVERE, "Exception while getting all the resources", exception);
    } 

    setAttribute(RESOURCES, resources);
    displayNotice();
    forward(Constants.MANAGE_RESOURCE_JSP);

  }

  /*
   * When request is made for edit operation, the operation to edit will be called. When the request is made to add
   * resource, the operation to add will be called.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    setRequestResponse(request, response);
    isAdmin();
    clearNotices();

    ResourceDao resourceDao = new ResourceDao();
    Resource resource = getForm(request);

    try {

      resource.sanitizeAndValidate();

      resourceDao.upsert(resource);
      addSuccessNotice("Resource has been saved successfully");
    } catch (ValidationServletException e) {
      addModalErrorNotice(e.getError());
      Gson gson = new Gson();
      setAttribute(FORM, gson.toJson(resource));
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while posting all the details of resources", exception);
      throw new ServletException("Error while trying to post details");
    }

    try {
      setAttribute(RESOURCES, resourceDao.getResource());
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Exception while posting all the details of resources", e);
      throw new ServletException("Error while trying to post details");
    }

    displayNotice();
    forward(Constants.MANAGE_RESOURCE_JSP);
  }

  @Override
  public Resource getForm(HttpServletRequest request) {
    Resource resource = new Resource();

    String resourcename = getParameter("resource_name");
    String description = getParameter("description");
    boolean isEnabled = "on".equals(getParameter("isenabled"));
    String timeLimitHours = getParameter("timeLimitHours").trim();
    String timeLimitMinutes = getParameter("timeLimitMinutes").trim();
    boolean isAllowedMultiple = "on".equals(getParameter("isAllowedMultiple"));
    Integer resourceId = NumberUtils.isCreatable(getParameter(RESOURCE_ID))
        ? NumberUtils.toInt(getParameter(RESOURCE_ID))
        : null;
        Integer maxUserBooking = NumberUtils.isCreatable(getParameter("max_user_booking"))
            ? NumberUtils.toInt(getParameter("max_user_booking"))
            : null;    
    boolean isAllowEmpOnProbation = "on".equals(getParameter("isAllowEmpOnProbation"));
    Integer timeInMinutes = (NumberUtils.toInt(timeLimitHours) * 60)
        + NumberUtils.toInt(timeLimitMinutes);

    resource.setRsResourceId(resourceId);
    resource.setResourceName(resourcename);
    resource.setResourceDescription(description);
    resource.setEnabled(isEnabled);
    resource.setTimeLimitHours(timeLimitHours);
    resource.setTimeLimitMinutes(timeLimitMinutes);
    resource.setIsAllowedMultiple(isAllowedMultiple);
    resource.setMaxUserBooking(maxUserBooking);
    resource.setIsPermanentEmployee(isAllowEmpOnProbation);
    resource.setTimeLimit(timeInMinutes == 0 ? null : timeInMinutes);

    return resource;
  }
}
