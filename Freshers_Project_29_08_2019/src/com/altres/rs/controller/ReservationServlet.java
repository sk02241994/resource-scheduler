package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.altres.mail.util.Mail;
import com.altres.rs.constants.Constants;
import com.altres.rs.dao.ReservationDao;
import com.altres.rs.dao.ResourceDao;
import com.altres.rs.model.ReservationDetails;
import com.altres.rs.model.Resource;
import com.google.gson.Gson;

/**
 * Servlet for making reservations, updating the reservations, or deleting the reservations from the database.
 */
@WebServlet("/ReservationServlet")
public class ReservationServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public ReservationServlet() {
    super();
  }

  private static final String MANAGE_RESERVATION = "rs-jsp/manage-reservation.jsp";
  private static final String RESERVATION_ID = "reservation_id";

  private static final Logger LOGGER = Logger.getLogger(ReservationServlet.class.getName());
  private static final String RESERVATIONS = "reservations";
  private static final String SINGLE_RESREVATION = "singleReservation";
  private static final String RESOURCES = "resources";
  private static final String SUCCESS_MESSAGE = "success_message";
  private static final String APPLICATION_JSON = "application/json";
  private static final String CALENDAR = "rs-jsp/calendar.jsp";
  private static final String SCHEDULE = "schedule";
  private static final String RESOURCE_NAME = "resource_name";
  private static final String USER_ID = "user_id";
  private static final String START_DATE = "start_date";
  private static final String START_TIME = "start_time";
  private static final String END_DATE = "end_date";
  private static final String END_TIME = "end_time";
  private static final String LOGIN_SERVLET_EMAIL = "login_servlet_email";
  
  
  /*
   * when the user logs in, the user will be show the schedules and accordingly the user will be able to book the
   * resource.
   * 
   * The user will be able to edit his own resources and will be able to view the reservations of other users.
   * 
   * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession();
    RequestDispatcher dispatcher = null;
   
   // CalenderEvent.test(request.getContextPath());
    
    ReservationDao reservationDao = new ReservationDao();
    ResourceDao resourceDao = new ResourceDao();
    String formAction = request.getParameter("form_action");
    int userId = (Integer) session.getAttribute("login_servlet_user_id");
    boolean isAdmin = (boolean) session.getAttribute("login_is_admin");

    try {
      switch (formAction == null ? "" : formAction) {
      case "edit":
        getDetailsForEditingReservation(request, response, reservationDao, resourceDao, userId, isAdmin);
        break;

      case "delete":
        deleteReservation(request, response, reservationDao, resourceDao, userId, isAdmin);
        break;

      case CALENDAR:
        response.setContentType(APPLICATION_JSON);
        request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
        request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
        dispatcher = request.getRequestDispatcher(CALENDAR);
        dispatcher.forward(request, response);
        break;

      case "calendarEdit":
        getDetailsForCalendarEdit(request, response, reservationDao, resourceDao, userId, isAdmin);
        break;

      case "calendarDelete":
        deleteReservationInCalendar(request, response, reservationDao, resourceDao, userId);
        break;

      case "":
        request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
        request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
        dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESERVATION_JSP);
        dispatcher.forward(request, response);
        break;

      default:
        break;
      }

    } catch (SQLException | ParseException exception) {
      LOGGER.log(Level.SEVERE, "Exception while getting all the reservations", exception);
      throw new ServletException("There was an error while trying to update your credentials");
    }

  }

  /**
   * Method will get all the details of the perticular reservation selected for editing. 
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param userId
   * @param isAdmin 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException
   * @throws ServletException
   */
  private void getDetailsForEditingReservation(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao, int userId, boolean isAdmin)
      throws SQLException, ParseException, IOException, ServletException {
    RequestDispatcher dispatcher = null;
    int editId = Integer.parseInt(request.getParameter(RESERVATION_ID));
    request.setAttribute(SINGLE_RESREVATION, reservationDao.getSingleReservation(editId, userId, isAdmin));
    request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
    request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
    dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESERVATION_JSP);
    dispatcher.forward(request, response);
  }

  /**
   * Method will delete the reservation from database.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param userId
   * @param isAdmin 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException
   * @throws ServletException
   */
  private void deleteReservation(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao, int userId, boolean isAdmin)
      throws SQLException, ParseException, IOException, ServletException {
    RequestDispatcher dispatcher = null;
    int deleteId = Integer.parseInt(request.getParameter(RESERVATION_ID));
    reservationDao.deleteReservation(deleteId, userId);
    request.setAttribute(SUCCESS_MESSAGE, "Reservation deleted successfully");
    request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
    request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
    dispatcher = request.getRequestDispatcher(Constants.MANAGE_RESERVATION_JSP);
    dispatcher.forward(request, response);
  }

  /**
   * Method used to get all the details of reservation for editing but will be only for calendar.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param userId
   * @param isAdmin 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException
   * @throws ServletException
   */
  private void getDetailsForCalendarEdit(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao, int userId, boolean isAdmin)
      throws SQLException, ParseException, IOException, ServletException {
    RequestDispatcher dispatcher = null;
    response.setContentType(APPLICATION_JSON);
    int editIdForCalendar = Integer.parseInt(request.getParameter(RESERVATION_ID));
    request.setAttribute(SINGLE_RESREVATION, reservationDao.getSingleReservation(editIdForCalendar, userId, isAdmin));
    request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
    request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
    dispatcher = request.getRequestDispatcher(CALENDAR);
    dispatcher.forward(request, response);
  }

  /**
   * Method for deleting the reservation shown in calendar.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param userId
   * @param isAdmin 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException
   * @throws ServletException
   */
  private void deleteReservationInCalendar(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao, int userId)
      throws SQLException, ParseException, IOException, ServletException {
    RequestDispatcher dispatcher = null;
    response.setContentType(APPLICATION_JSON);
    int deleteIdForCalendar = Integer.parseInt(request.getParameter(RESERVATION_ID));
    reservationDao.deleteReservation(deleteIdForCalendar, userId);
    request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
    request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
    dispatcher = request.getRequestDispatcher(CALENDAR);
    dispatcher.forward(request, response);
  }

  /**
   * Method for converting the data from database to json string format.
   * 
   * @param reservationListing
   * @return String format of json data
   */
  private String convertListToJson(List<ReservationDetails> reservationListing) {

    Gson gson = new Gson();
    return gson.toJson(reservationListing);
  }

  /*
   * the user will be shown the available resources and will be able to reserve the resource for whole day or specific
   * time provided that the resource is free at that particular time.
   * 
   * If the resources are already booked the user will be shown error that the resource are already booked if the user
   * enters date and time in wrong format there will be error telling the user that the date and time format is wrong.
   * 
   * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
   * javax.servlet.http.HttpServletResponse)
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    ReservationDao reservationDao = new ReservationDao();
    ResourceDao resourceDao = new ResourceDao();

    String editReservationField = request.getParameter("edit_reservation");

    try {

      switch (editReservationField == null ? "" : editReservationField) {

      case "edit_reservation":
        editReservationDetails(request, response, reservationDao, resourceDao);
        break;

      case "edit_calendar_reservation":
        editReservationDetailsInCalendar(request, response, reservationDao, resourceDao);
        break;

      case "add_form_calendar":
        saveNewReservationsDetailFromCalendar(request, response, reservationDao, resourceDao);
        break;
        
      case "":
        saveNewReservationsDetail(request, response, reservationDao, resourceDao);
        break;

      default:
        break;
      }
      
    } catch (ParseException e) {
      throw new ServletException("There was an error in date and time format");
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while booking a resource and time", exception);
      throw new ServletException("There was an error while trying to save your credentials");
    }
  }

  /**
   * Method will get all the reservation detail changes made in particular reservation id by particular user id 
   * and will update in database.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param dateFormat
   * @throws SQLException
   * @throws IOException
   * @throws ParseException
   * @throws ServletException
   */
  private void editReservationDetails(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao)
      throws SQLException, IOException, ParseException, ServletException {

    ReservationDetails reservation = new ReservationDetails();
    HttpSession session = request.getSession();

    RequestDispatcher dispatcher = null;
    int resourceId = Integer.parseInt(request.getParameter(RESOURCE_NAME));
    int userId = Integer.parseInt(request.getParameter(USER_ID));
    String startDate = request.getParameter(START_DATE);
    String startTime = request.getParameter(START_TIME) + ":00";
    String endDate = request.getParameter(END_DATE);
    String endTime = request.getParameter(END_TIME) + ":00";
    int reservationId = Integer.parseInt(request.getParameter(RESERVATION_ID));

    Resource resource = new ResourceDao().getSingleResource(resourceId);

    LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T" + startTime);
    LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T" + endTime);

    if (endDateTime.isBefore(startDateTime)) {

      request.setAttribute("error_message", "End date and time cannot be smaller than start date and time");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    } else if (startDateTime.until(endDateTime, ChronoUnit.MINUTES) < 10
        && startDateTime.toLocalTime().until(endDateTime.toLocalTime(), ChronoUnit.MINUTES) < 10) {

      request.setAttribute("error_message",
          "Difference between end date and start date cannot be less than 10 minutes");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    } else if (isReservationUnderLimit(startDateTime, endDateTime, resource)) {
      request.setAttribute("error_message", errorMessage(resource));
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (isAllowedToSaveMultipleRecordsInADay(reservationDao, resource, startDateTime, endDateTime, userId,
        resourceId)) {
      request.setAttribute("error_message", "Cannot reserve the resource more than once");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else {
      reservation.setUserId(userId);
      reservation.setResourceId(resourceId);
      reservation.setStartDate(startDate);
      reservation.setStartTime(startTime);
      reservation.setEndDate(endDate);
      reservation.setEndTime(endTime);
      reservation.setUserName((String) session.getAttribute(LOGIN_SERVLET_EMAIL));

      if (reservationDao.isReserved(startDateTime, endDateTime, resourceId, reservationId)) {
        reservationDao.updateReservation(reservation, reservationId);
        if(reservationId != 0) {
          Mail mail = new Mail((String)session.getAttribute("login_servlet_email"));
          if(!mail.sendMessageToUser(reservationId) || !mail.setMessageToAdmins(reservationId)) {
            request.setAttribute("error_message", "Unable to send mail");
          }
        }
        request.setAttribute(SUCCESS_MESSAGE, "Date and time has been updated");
      } else {
        request.setAttribute("error_message", "Date and time already reserved for resource please try another date");
      }
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    }
  }

  /**
   * Method for updating the details requested from calendar page.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param dateFormat
   * @throws SQLException
   * @throws IOException
   * @throws ParseException
   * @throws ServletException
   */
  private void editReservationDetailsInCalendar(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao)
      throws SQLException, IOException, ParseException, ServletException {

    ReservationDetails reservation = new ReservationDetails();
    RequestDispatcher dispatcher = null;
    HttpSession session = request.getSession();
    boolean isAdmin = (boolean) session.getAttribute("login_is_admin");

    String startDate = request.getParameter(START_DATE);
    String startTime = request.getParameter(START_TIME) + ":00";
    String endDate = request.getParameter(END_DATE);
    String endTime = request.getParameter(END_TIME) + ":00";
    int resourceId = Integer.parseInt(request.getParameter(RESOURCE_NAME));
    int userId = Integer.parseInt(request.getParameter(USER_ID));
    int reservationIdFromCalendar = Integer.parseInt(request.getParameter(RESERVATION_ID));

    Resource resource = new ResourceDao().getSingleResource(resourceId);

    LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T" + startTime);
    LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T" + endTime);

    if (endDateTime.isBefore(startDateTime)) {

      request.setAttribute("error_message", "End date and time cannot be smaller than start date and time");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SINGLE_RESREVATION, reservationDao.getSingleReservation(reservationIdFromCalendar, userId, isAdmin));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (startDateTime.toLocalTime().until(endDateTime.toLocalTime(), ChronoUnit.MINUTES) < 10) {

      request.setAttribute("error_message",
          "Difference between end date and start date cannot be less than 10 minutes");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SINGLE_RESREVATION, reservationDao.getSingleReservation(reservationIdFromCalendar, userId, isAdmin));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (isReservationUnderLimit(startDateTime, endDateTime, resource)) {
      request.setAttribute("error_message", errorMessage(resource));
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    }  else if (isAllowedToSaveMultipleRecordsInADay(reservationDao, resource, startDateTime, endDateTime, userId,
        resourceId)) {
      request.setAttribute("error_message", "Cannot reserve the resource more than once");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else {

      reservation.setUserId(userId);
      reservation.setResourceId(resourceId);
      reservation.setStartDate(startDate);
      reservation.setStartTime(startTime);
      reservation.setEndDate(endDate);
      reservation.setEndTime(endTime);
      reservation.setUserName((String) session.getAttribute(LOGIN_SERVLET_EMAIL));

      if (reservationDao.isReserved(startDateTime, endDateTime, resourceId, reservationIdFromCalendar)) {
        reservationDao.updateReservation(reservation, reservationIdFromCalendar);
        if(reservationIdFromCalendar != 0) {
          Mail mail = new Mail((String)session.getAttribute("login_servlet_email"));
          if(!mail.sendMessageToUser(reservationIdFromCalendar) || !mail.setMessageToAdmins(reservationIdFromCalendar)) {
            request.setAttribute("error_message", "Unable to send mail");
          }
        }
        request.setAttribute(SUCCESS_MESSAGE, "Date and time has been updated");
      } else {
        request.setAttribute("error_message", "Date and time already reserved for resource please try another date");
      }
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    }
  }

  /**
   * Method for saving new reservation requested.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param dateFormat
   * @throws SQLException
   * @throws IOException
   * @throws ParseException
   * @throws ServletException
   */
  private void saveNewReservationsDetail(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao)
      throws SQLException, IOException, ParseException, ServletException {

    ReservationDetails reservation = new ReservationDetails();
    RequestDispatcher dispatcher = null;
    HttpSession session = request.getSession();

    String startDate = request.getParameter(START_DATE);
    String startTime = request.getParameter(START_TIME) + ":00";
    String endDate = request.getParameter(END_DATE);
    String endTime = request.getParameter(END_TIME) + ":00";
    int resourceId = Integer.parseInt(request.getParameter(RESOURCE_NAME));
    int userId = Integer.parseInt(request.getParameter(USER_ID));

    Resource resource = new ResourceDao().getSingleResource(resourceId);

    LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T" + startTime);
    LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T" + endTime);

    if (endDateTime.isBefore(startDateTime)) {
      request.setAttribute("error_message", "End date and time cannot be smaller than start date and time");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    } else if (startDateTime.toLocalTime().until(endDateTime.toLocalTime(), ChronoUnit.MINUTES) < 10) {
      request.setAttribute("error_message",
          "Difference between end date and start date cannot be less than 10 minutes");
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    } else if (isReservationUnderLimit(startDateTime, endDateTime, resource)) {
      request.setAttribute("error_message", errorMessage(resource));
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (isAllowedToSaveMultipleRecordsInADay(reservationDao, resource, startDateTime, endDateTime, userId,
        resourceId)) {
      request.setAttribute("error_message", "Cannot reserve the resource more than once");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else {
      reservation.setUserId(userId);
      reservation.setResourceId(resourceId);
      reservation.setStartDate(startDate);
      reservation.setStartTime(startTime);
      reservation.setEndDate(endDate);
      reservation.setEndTime(endTime);
      reservation.setUserName((String) session.getAttribute(LOGIN_SERVLET_EMAIL));

      if (reservationDao.isReserved(startDateTime, endDateTime, resourceId)) {

        int reservationId = reservationDao.saveReservation(reservation);
        if(reservationId != 0) {
          Mail mail = new Mail((String)session.getAttribute("login_servlet_email"));
          if(!mail.sendMessageToUser(reservationId) || !mail.setMessageToAdmins(reservationId)) {
            request.setAttribute("error_message", "Unable to send mail");
          }
        }

        request.setAttribute(SUCCESS_MESSAGE, "Date and time has been added");
      } else {
        request.setAttribute("error_message", "Date and time already reserved for resource please try another date");
      }
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(RESERVATIONS, reservationDao.getReservationListing());
      dispatcher = request.getRequestDispatcher(MANAGE_RESERVATION);
      dispatcher.forward(request, response);
    }
  }
  
  /**
   * Method add new reservation from calendar.
   * 
   * @param request
   * @param response
   * @param reservationDao
   * @param resourceDao
   * @param dateFormat
   * @throws ParseException
   * @throws SQLException
   * @throws IOException
   * @throws ServletException
   */
  private void saveNewReservationsDetailFromCalendar(HttpServletRequest request, HttpServletResponse response,
      ReservationDao reservationDao, ResourceDao resourceDao) 
          throws ParseException, SQLException, IOException, ServletException {
    
    ReservationDetails reservation = new ReservationDetails();
    RequestDispatcher dispatcher = null;
    HttpSession session = request.getSession();

    String startDate = request.getParameter(START_DATE);
    String startTime = request.getParameter(START_TIME) + ":00";
    String endDate = request.getParameter(END_DATE);
    String endTime = request.getParameter(END_TIME) + ":00";
    int resourceId = Integer.parseInt(request.getParameter(RESOURCE_NAME));
    int userId = Integer.parseInt(request.getParameter(USER_ID));

    Resource resource = new ResourceDao().getSingleResource(resourceId);

    LocalDateTime startDateTime = LocalDateTime.parse(startDate + "T" + startTime);
    LocalDateTime endDateTime = LocalDateTime.parse(endDate + "T" + endTime);

    if (endDateTime.isBefore(startDateTime)) {
      request.setAttribute("error_message", "End date and time cannot be smaller than start date and time");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (startDateTime.toLocalTime().until(endDateTime.toLocalTime(), ChronoUnit.MINUTES) < 10) {
      request.setAttribute("error_message",
          "Difference between end date and start date cannot be less than 10 minutes");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (isReservationUnderLimit(startDateTime, endDateTime, resource)) {
      request.setAttribute("error_message", errorMessage(resource));
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else if (isAllowedToSaveMultipleRecordsInADay(reservationDao, resource, startDateTime, endDateTime, userId,
        resourceId)) {
      request.setAttribute("error_message", "Cannot reserve the resource more than once");
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    } else {
      reservation.setUserId(userId);
      reservation.setResourceId(resourceId);
      reservation.setStartDate(startDate);
      reservation.setStartTime(startTime);
      reservation.setEndDate(endDate);
      reservation.setEndTime(endTime);
      reservation.setUserName((String) session.getAttribute(LOGIN_SERVLET_EMAIL));

      if (reservationDao.isReserved(startDateTime, endDateTime, resourceId)) {

        int reservationId = reservationDao.saveReservation(reservation);
        if(reservationId != 0) {
          Mail mail = new Mail((String)session.getAttribute("login_servlet_email"));
          if(!mail.sendMessageToUser(reservationId) || !mail.setMessageToAdmins(reservationId)) {
            request.setAttribute("error_message", "Unable to send mail");
          }
        }

        request.setAttribute(SUCCESS_MESSAGE, "Date and time has been added");
      } else {
        request.setAttribute("error_message", "Date and time already reserved for resource please try another date");
      }
      response.setContentType(APPLICATION_JSON);
      request.setAttribute(RESOURCES, resourceDao.getResourceForUser());
      request.setAttribute(SCHEDULE, convertListToJson(reservationDao.getReservationListing()));
      dispatcher = request.getRequestDispatcher(CALENDAR);
      dispatcher.forward(request, response);
    }
  }

  /**
   * Method to check if the reservation is under the time limit.
   * 
   * @param startTime
   * @param endTime
   * @param resource
   * @return
   */
  private boolean isReservationUnderLimit(LocalDateTime startTime, LocalDateTime endTime, Resource resource) {

    return resource.getTimeLimit() != null && resource.getTimeLimit() != 0
        && endTime.toLocalTime().get(ChronoField.MINUTE_OF_DAY)
            - startTime.toLocalTime().get(ChronoField.MINUTE_OF_DAY) > resource.getTimeLimit(); 
  }

  private String errorMessage(Resource resource) {
    int hours = (int) resource.getTimeLimit() / 60;
    int minutes = (int) resource.getTimeLimit() % 60;

    StringBuilder errorMessage = new StringBuilder();
    errorMessage.append("Resource cannot be booked beyond ");

    if(hours != 0) {
      errorMessage.append(hours + " hours ");
    }
    if(minutes != 0) {
      errorMessage.append(hours + " minutes");
    }
    return errorMessage.toString();
  }

  private boolean isAllowedToSaveMultipleRecordsInADay(ReservationDao reservationDao, Resource resource,
      LocalDateTime startDateTime, LocalDateTime endDateTime, int userId, int resourceId) throws SQLException, IOException {
    
    return !resource.isAllowedMultiple() && reservationDao.isUsedInDay(startDateTime, endDateTime, resourceId, userId);
    
  }
}