package com.altres.rs.model;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;

import com.altres.rs.dao.ReservationDao;
import com.altres.rs.dao.ResourceDao;
import com.altres.utils.PojoDeletable;
import com.altres.utils.PojoSavable;
import com.altres.utils.exception.ValidationServletException;

/**
 * Class that contains getter and setter for reservation details.
 */
public class Reservation implements PojoSavable<Void>, PojoDeletable<Void> {

  private Integer reservationId;
  private int userId;
  private int resourceId;
  private String startDate;
  private String startTime;
  private String endDate;
  private String endTime;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getResourceId() {
    return resourceId;
  }

  public void setResourceId(int resourceId) {
    this.resourceId = resourceId;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  @Override
  public void validateDelete(Void variable) throws ValidationServletException {
    // Not implemented.
  }

  @Override
  public void sanitize() {
    // Not implemented.
  }

  private List<String> validateDateTime() {
    List<String> errors = new ArrayList<>();
    if (StringUtils.isBlank(getStartDate())) {
      errors.add("Please select a valid start date.");
    }
    if (StringUtils.isBlank(getStartTime())) {
      errors.add("Please select a valid start time.");
    }

    if (StringUtils.isBlank(getEndDate())) {
      errors.add("Please select a valid end date.");
    }
    if (StringUtils.isBlank(getEndTime())) {
      errors.add("Please select a valid end date.");
    }
    return errors;
  }

  @Override
  public void validate(Void variable) throws ValidationServletException, ServletException {

    List<String> errors = new ArrayList<>();
    ReservationDao reservationDao = new ReservationDao();

    errors.addAll(validateDateTime());

    if (errors.isEmpty()) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      LocalDateTime startDateTime = LocalDateTime.parse(getStartDate() + " " + getStartTime(), formatter);
      LocalDateTime endDateTime = LocalDateTime.parse(getEndDate() + " " + getEndTime(), formatter);

      if (endDateTime.isBefore(startDateTime)) {
        errors.add("End date and time cannot be before start date and time.");
      }

      if (startDateTime.until(endDateTime, ChronoUnit.MINUTES) < 10
          && startDateTime.toLocalTime().until(endDateTime.toLocalTime(), ChronoUnit.MINUTES) < 10) {
        errors.add("Difference between end date and start date cannot be less than 10 minutes");
      }

      try {
        Resource resource = new ResourceDao().getSingleResource(getResourceId());

        if (isReservationUnderLimit(startDateTime, endDateTime, resource)) {
          errors.add(errorMessage(resource));
        }

        if (isAllowedToSaveMultipleRecordsInADay(reservationDao, resource, startDateTime, endDateTime)) {
          errors.add("Cannot reserve the resource more than once");
        }

        if (isReserved(startDateTime, endDateTime, reservationDao)) {
          errors.add("Date and time already reserved for resource please try another date and time.");
        }

      } catch (SQLException | IOException | ParseException e) {
        throw new ServletException(e);
      }
    }

    if (!errors.isEmpty()) {
      throw new ValidationServletException(errors);
    }
  }

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

    if (hours != 0) {
      errorMessage.append(hours + " hours ");
    }
    if (minutes != 0) {
      errorMessage.append(minutes + " minutes");
    }
    return errorMessage.toString();
  }

  private boolean isAllowedToSaveMultipleRecordsInADay(ReservationDao reservationDao, Resource resource,
      LocalDateTime startDateTime, LocalDateTime endDateTime) throws SQLException, IOException, ParseException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    return !resource.isAllowedMultiple()
        && reservationDao.isUsedInDay(LocalDateTime.now(), getResourceId(), getUserId()).stream()
        .anyMatch(e -> {
          boolean isAllowedMultiple = (
              (startDateTime.isAfter(LocalDate.parse(e.getStartDate(), formatter).atStartOfDay())
              && startDateTime.isBefore(LocalDate.parse(e.getEndDate(), formatter).atTime(LocalTime.MAX))) 
              || (endDateTime.isAfter(LocalDate.parse(e.getStartDate(), formatter).atStartOfDay())
                  && endDateTime.isBefore(LocalDate.parse(e.getEndDate(), formatter).atTime(LocalTime.MAX))))
              && getResourceId() == e.getResourceId();
          if (getReservationId() != null) {
            isAllowedMultiple = isAllowedMultiple && !e.getReservationId().equals(getReservationId());
          }
          return isAllowedMultiple;
        });
  }

  private boolean isReserved(LocalDateTime startTime, LocalDateTime endTime, ReservationDao reservationDao)
      throws SQLException, IOException {
    List<Reservation> reservations = reservationDao.findAllByStartDateAndResource(LocalDateTime.now(), getResourceId());
    return reservations.stream()
        .anyMatch(e -> {
          boolean isBooked = ((startTime.isAfter(LocalDateTime.parse(e.getStartDate()))
              && startTime.isBefore(LocalDateTime.parse(e.getEndDate())))
              || (endTime.isAfter(LocalDateTime.parse(e.getStartDate()))
                  && endTime.isBefore(LocalDateTime.parse(e.getEndDate()))))
              && (getResourceId() == e.getResourceId());
          if (getReservationId() != null) {
            isBooked = isBooked && !(getReservationId().equals(e.getReservationId()));
          }
          return isBooked;
        });
  }
}
