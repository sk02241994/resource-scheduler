package com.altres.rs.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
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

import com.altres.mail.util.Mail;
import com.altres.rs.constants.Constants;
import com.altres.rs.dao.ReservationDao;
import com.altres.rs.dao.ResourceDao;
import com.altres.rs.model.Reservation;
import com.altres.rs.model.ReservationDetails;
import com.altres.rs.model.Resource;
import com.altres.rs.model.User;
import com.altres.utils.ResourceSchedulerServlet;
import com.altres.utils.exception.ValidationServletException;
import com.google.gson.Gson;

/**
 * Servlet for making reservations, updating the reservations, or deleting the reservations from the database.
 */
@WebServlet("/ReservationServlet")
public class ReservationServlet extends ResourceSchedulerServlet<ReservationDetails> {
  private static final String FORM_ACTION = "form_action";
  private static final String FORM_TYPE = "form_type";
  private static final long serialVersionUID = 1L;

  public ReservationServlet() {
    super();
  }

  private static final String RESERVATION_ID = "reservation_id";

  private static final Logger LOGGER = Logger.getLogger(ReservationServlet.class.getName());
  private static final String RESERVATIONS = "reservations";
  private static final String RESREVATION = "reservation";
  private static final String RESOURCES = "resources";
  private static final String APPLICATION_JSON = "application/json";
  private static final String SCHEDULE = "schedule";
  private static final String RESOURCE_NAME = "resource_name";
  private static final String START_DATE = "start_date";
  private static final String START_TIME = "start_time";
  private static final String END_DATE = "end_date";
  private static final String END_TIME = "end_time";

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

    setRequestResponse(request, response);
    clearNotices();

    ReservationDao reservationDao = new ReservationDao();
    ResourceDao resourceDao = new ResourceDao();
    String formType = getParameter(FORM_TYPE);
    String formAction = getParameter(FORM_ACTION);
    User user = (User) getSessionAttribute(LoginServlet.LOGGEDIN_USER);
    int userId = user.getRsUserId();
    boolean isAdmin = user.isAdmin();


    try {

      if (StringUtils.equals("edit", formAction) && StringUtils.isNotBlank(getParameter(RESERVATION_ID))) {
        Gson gson = new Gson();
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(reservationDao.getSingleReservation(NumberUtils.toInt(getParameter(RESERVATION_ID)), userId, isAdmin)));
        return;
      }

      if (StringUtils.equals("delete", formAction) && StringUtils.isNotBlank(getParameter(RESERVATION_ID))) {
        Reservation reservation = reservationDao.getSingleReservation(NumberUtils.toInt(getParameter(RESERVATION_ID)), userId, user.isAdmin());
        reservation.validateDelete(userId);
        reservationDao.deleteReservation(NumberUtils.toInt(getParameter(RESERVATION_ID)), userId);
        addSuccessNotice("Reservation deleted successfully");
      }

    }  catch (ValidationServletException e) {
      addErrorNotice(e.getError());
    } catch (SQLException | ParseException exception) {
      LOGGER.log(Level.SEVERE, "Exception while getting all the reservations", exception);
      throw new ServletException("There was an error while trying to update your credentials");
    }

    setReservationListing(resourceDao, reservationDao, formType, response);
    displayNotice();
    forward(StringUtils.isNotBlank(formType) && StringUtils.equals("calendar", formType) ? Constants.CALENDAR
        : Constants.MANAGE_RESERVATION_JSP);

  }

  private void setReservationListing(ResourceDao resourceDao, ReservationDao reservationDao, String formType,
      HttpServletResponse response) throws ServletException {
    List<Resource> resources = new ArrayList<>();
    List<ReservationDetails> reservationDetails = new ArrayList<>();
    try {
      reservationDetails.addAll(reservationDao.getReservationListing());
      if (StringUtils.isNotBlank(formType) && StringUtils.equals("calendar", formType)) {
        response.setContentType(APPLICATION_JSON);
        setAttribute(SCHEDULE, convertListToJson(reservationDetails));
      } else {
        setAttribute(RESERVATIONS, reservationDetails);
      }
      resources.addAll(resourceDao.getResourceForUser());
      setAttribute(RESOURCES, resources);
    } catch (SQLException | ParseException | IOException e) {
      LOGGER.log(Level.SEVERE, "Exception while getting all the reservations", e);
      throw new ServletException("There was an error while trying to update your credentials");
    }

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

    setRequestResponse(request, response);
    clearNotices();

    ReservationDao reservationDao = new ReservationDao();
    ResourceDao resourceDao = new ResourceDao();

    try {
      editReservationDetails(request, response, reservationDao, resourceDao);

    } catch (ParseException e) {
      throw new ServletException("There was an error in date and time format");
    } catch (SQLException exception) {
      LOGGER.log(Level.SEVERE, "Exception while booking a resource and time", exception);
      throw new ServletException("There was an error while trying to save your credentials");
    }
  }

  /**
   * Method will get all the reservation detail changes made in particular reservation id by particular user id and will
   * update in database.
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

    ReservationDetails reservation = getForm(request);
    String editReservationField = getParameter("edit_reservation");
    User user = (User) getSessionAttribute(LoginServlet.LOGGEDIN_USER);

    boolean isMailSent = false;

    try {
      reservation.sanitizeAndValidate();
      Integer reservationId = reservationDao.upsertReservation(reservation);
      reservation.setReservationId(reservationId);
      if (reservationId != null) {
        Mail mail = new Mail(user.getEmail_address());
        isMailSent = true;
        if (!mail.sendMessageToUser(reservationId) || !mail.setMessageToAdmins(reservationId)) {
          addModalErrorNotice("Unable to send mail");
        }
      }
    } catch (ValidationServletException e) {
      addModalErrorNotice(e.getError());
      Gson gson = new Gson();
      setAttribute(RESREVATION, gson.toJson(reservation));
    }

    if(isMailSent) {
      addSuccessNotice("The resource has been successfully booked.");
    }

    List<ReservationDetails> reservationDetails = reservationDao.getReservationListing(); 
    setAttribute(RESOURCES, resourceDao.getResourceForUser());
    setAttribute(RESERVATIONS, reservationDetails);

    response.setContentType(APPLICATION_JSON);
    setAttribute(SCHEDULE, convertListToJson(reservationDetails));

    displayNotice();
    forward(StringUtils.equals("edit_calendar_reservation", editReservationField) ? Constants.CALENDAR
        : Constants.MANAGE_RESERVATION_JSP);
  }

  @Override
  public ReservationDetails getForm(HttpServletRequest request) {

    ReservationDetails reservation = new ReservationDetails();

    User user = (User) getSessionAttribute(LoginServlet.LOGGEDIN_USER);
    Integer resourceId = NumberUtils.toInt(getParameter(RESOURCE_NAME));
    int userId = StringUtils.isNotBlank(getParameter("user_id")) ? NumberUtils.toInt(getParameter("user_id"))
        : user.getRsUserId();
    String startDate = getParameter(START_DATE);
    String startTime = request.getParameter(START_TIME);
    String endDate = request.getParameter(END_DATE);
    String endTime = request.getParameter(END_TIME);
    Integer reservationId = NumberUtils.isCreatable(getParameter(RESERVATION_ID))
        ? NumberUtils.toInt(getParameter(RESERVATION_ID))
        : null;

    reservation.setUserId(userId);
    reservation.setResourceId(resourceId);
    reservation.setStartDate(startDate);
    reservation.setStartTime(startTime);
    reservation.setEndDate(endDate);
    reservation.setEndTime(endTime);
    reservation.setUserName(user.getEmail_address());
    reservation.setReservationId(reservationId);

    return reservation;
  }
}