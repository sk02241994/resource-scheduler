package com.altres.rs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.altres.connection.util.SqlConnection;
import com.altres.rs.model.Reservation;
import com.altres.rs.model.ReservationDetails;

/**
 * Dao for handling all the requests coming for inserting new reservation, updating existing reservation 
 * and deleting a reservation.
 */
public class ReservationDao {

  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  private static final String COL_RS_RESERVATION_ID = "rs_reservation_id";
  private static final String COL_RS_USER_ID = "rs_user_id";
  private static final String COL_NAME = "name";
  private static final String COL_RESOURCE_NAME = "resource_name";
  private static final String COL_RS_RESOURCE_ID = "rs_resource_id";
  private static final String COL_START_DATE = "start_date";
  private static final String COL_END_DATE = "end_date";

  /**
   * Method to return all the reservations in database.
   * 
   * @return List<Reservation>
   * @throws SQLException
   * @throws ParseException
   * @throws IOException 
   */
  public List<ReservationDetails> getReservationListing() throws SQLException, ParseException, IOException {

    List<ReservationDetails> reservationList = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
      Date dateTime = null;
      DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
      DateFormat time = new SimpleDateFormat("HH:mm");

      String queryForReservation = "SELECT rs_reservation.rs_reservation_id, " + "rs_reservation.rs_user_id, "
          + "rs_user.name, " + "rs_reservation.rs_resource_id, "
          + "rs_resource.resource_name, " + "rs_reservation.start_date, " + "rs_reservation.end_date "
          + "FROM rs_user, rs_resource, rs_reservation " + "WHERE rs_user.rs_user_id = rs_reservation.rs_user_id AND "
          + "rs_resource.rs_resource_id = rs_reservation.rs_resource_id ";

      statement = connection.prepareStatement(queryForReservation);

      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        ReservationDetails reservationDetails = new ReservationDetails();

        reservationDetails.setReservationId(resultSet.getInt(COL_RS_RESERVATION_ID));

        reservationDetails.setUserId(resultSet.getInt(COL_RS_USER_ID));

        reservationDetails.setUserName(resultSet.getString(COL_NAME));

        reservationDetails.setResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));

        reservationDetails.setResourceName(resultSet.getString(COL_RESOURCE_NAME));

        dateTime = inputFormat.parse(resultSet.getString(COL_START_DATE));
        reservationDetails.setStartDate(date.format(dateTime));

        reservationDetails.setStartTime(time.format(dateTime));

        dateTime = inputFormat.parse(resultSet.getString(COL_END_DATE));
        reservationDetails.setEndDate(date.format(dateTime));

        reservationDetails.setEndTime(time.format(dateTime));

        reservationList.add(reservationDetails);
      }

    } finally {
            
      if (resultSet != null) {
        resultSet.close();
      }
      
      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }

    return reservationList;
  }

  /**
   * Method to save the resources that are selected and the time for when the resources are booked.
   * 
   * @param reservation
   * @return 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException 
   */
  public int saveReservation(ReservationDetails reservation) throws SQLException, ParseException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet generatedKeys = null;
    int reservationId = 0;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_PATTERN);
      DateFormat dateTimeParser = new SimpleDateFormat(DATE_TIME_PATTERN); // changes made here
      Date date = null;

      String queryToSaveReservation = "INSERT INTO rs_reservation "
          + "(rs_user_id, rs_resource_id, start_date, end_date, created_by, created_date, updated_by, updated_date) "
          + "VALUES (?, ?, ?, ?, ?, now(), ?, now())";

      statement = connection.prepareStatement(queryToSaveReservation, Statement.RETURN_GENERATED_KEYS);

      statement.setInt(1, reservation.getUserId());
      statement.setInt(2, reservation.getResourceId());

      date = dateTimeParser.parse(reservation.getStartDate() + " " + reservation.getStartTime());
      statement.setString(3, dateTimeFormatter.format(date));

      date = dateTimeParser.parse(reservation.getEndDate() + " " + reservation.getEndTime());
      statement.setString(4, dateTimeFormatter.format(date));

      statement.setString(5, reservation.getUserName());
      statement.setString(6, reservation.getUserName());

      statement.executeUpdate();
      generatedKeys = statement.getGeneratedKeys();
      boolean isGenerated = generatedKeys.next();
      if(isGenerated) {
        reservationId = generatedKeys.getInt(1);
      }
    } finally {
      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
      
      if(generatedKeys != null) {
        generatedKeys.close();
      }
    }

    return reservationId;
  }

  /**
   * Method to check if the resources that are being booked are already reserved.
   * 
   * @param startDateTime
   * @param endDateTime
   * @param resourceId
   * @return boolean
   * @throws SQLException
   * @throws IOException 
   */
  public boolean isReserved(LocalDateTime startDateTime, LocalDateTime endDateTime, int resourceId) throws SQLException, IOException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    boolean isNonReserved =  true;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String queryToFindValidDate = "SELECT * FROM rs_reservation WHERE ((start_date BETWEEN ? AND ?) OR "
          + "(end_date BETWEEN ? AND ?)) AND rs_resource_id = ?";

      statement = connection.prepareStatement(queryToFindValidDate);

      statement.setString(1, startDateTime.format(formatter));
      statement.setString(2, endDateTime.format(formatter));
      statement.setString(3, startDateTime.format(formatter));
      statement.setString(4, endDateTime.format(formatter));
      statement.setInt(5, resourceId);

      resultSet = statement.executeQuery();
      if (resultSet.next()) {
        isNonReserved = false;
      }
      
    } finally {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }

    return isNonReserved;
  }

  /**
   * Method to delete reservation.
   * 
   * @param resourceId
   * @throws SQLException
   * @throws IOException 
   */
  public void deleteReservation(int resourceId, int userId) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String sqlQueryToDelete = "DELETE FROM rs_reservation WHERE rs_reservation_id = ? AND rs_user_id = ?";

      statement = connection.prepareStatement(sqlQueryToDelete);

      statement.setInt(1, resourceId);
      statement.setInt(2, userId);

      statement.executeUpdate();

    } finally {
      if (statement != null) {

        statement.close();

      }

      if (connection != null) {

        connection.close();

      }
    }
  }

  /**
   * Method to update the reservation details.
   * 
   * @param reservation
   * @param reservationId
   * @return 
   * @throws SQLException
   * @throws ParseException
   * @throws IOException 
   */
  public void updateReservation(Reservation reservation, int reservationId)
      throws SQLException, ParseException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat dateTimeFormatter = new SimpleDateFormat(DATE_TIME_PATTERN);
      DateFormat dateTimeParser = new SimpleDateFormat(DATE_TIME_PATTERN);
      Date date = null;

      String queryForUpdate = "UPDATE rs_reservation SET rs_reservation_id = ?, rs_resource_id = ?, start_date = ?, end_date = ? WHERE "
          + "rs_reservation_id = ? ";

      statement = connection.prepareStatement(queryForUpdate);

      statement.setInt(1, reservationId);
      statement.setInt(2, reservation.getResourceId());

      date = dateTimeParser.parse(reservation.getStartDate() + " " + reservation.getStartTime());
      statement.setString(3, dateTimeFormatter.format(date));

      date = dateTimeParser.parse(reservation.getEndDate() + " " + reservation.getEndTime());
      statement.setString(4, dateTimeFormatter.format(date));

      statement.setInt(5, reservationId);

      statement.executeUpdate();
    } finally {
      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }
  }

  /**
   * Method to check if the resources are reserved before updating.
   * 
   * @param startDateTime
   * @param endDateTime
   * @param resourceId
   * @param reservationId
   * @return boolean
   * @throws SQLException
   * @throws IOException 
   */
  public boolean isReserved(LocalDateTime startDateTime, LocalDateTime endDateTime, int resourceId, int reservationId)
      throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String queryToFindValidDate = "SELECT * FROM rs_reservation WHERE ((start_date BETWEEN ? AND ?) OR "
          + "(end_date BETWEEN ? AND ?)) AND rs_resource_id = ? AND rs_reservation_id != ?";

      statement = connection.prepareStatement(queryToFindValidDate);

      statement.setString(1, Timestamp.valueOf(startDateTime).toString().replaceAll("\\.\\d+", ""));
      statement.setString(2, Timestamp.valueOf(endDateTime).toString().replaceAll("\\.\\d+", ""));
      statement.setString(3, Timestamp.valueOf(startDateTime).toString().replaceAll("\\.\\d+", ""));
      statement.setString(4, Timestamp.valueOf(endDateTime).toString().replaceAll("\\.\\d+", ""));
      statement.setInt(5, resourceId);
      statement.setInt(6, reservationId);

      resultSet = statement.executeQuery();

      if (resultSet.next()) {
        return false;
      }
    } finally {

      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }

    return true;

  }

  /**
   * Method to get single reservation detail to be displayed.
   * 
   * @param reservationId
   * @param isAdmin 
   * @return ReservationDetails
   * @throws SQLException
   * @throws ParseException
   * @throws IOException 
   */
  public ReservationDetails getSingleReservation(int reservationId, int userId, boolean isAdmin)
      throws SQLException, ParseException, IOException {

    ReservationDetails reservationDetails = new ReservationDetails();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
      Date dateTime = null;
      DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
      DateFormat time = new SimpleDateFormat("HH:mm");

      String queryForReservation = "SELECT rs_reservation.rs_reservation_id, " + "rs_reservation.rs_user_id, "
          + "rs_user.name, " + "rs_reservation.rs_resource_id, "
          + "rs_resource.resource_name, " + "rs_reservation.start_date, " + "rs_reservation.end_date "
          + "FROM rs_user, rs_resource, rs_reservation " + "WHERE rs_user.rs_user_id = rs_reservation.rs_user_id AND "
          + "rs_resource.rs_resource_id = rs_reservation.rs_resource_id " + "AND rs_reservation.rs_reservation_id = ?"
          + (!isAdmin ? " AND rs_reservation.rs_user_id = ?" : "");

      statement = connection.prepareStatement(queryForReservation);

      statement.setInt(1, reservationId);
      if(!isAdmin) {
        statement.setInt(2, userId);
      }

      resultSet = statement.executeQuery();

      while (resultSet.next()) {

        reservationDetails.setReservationId(resultSet.getInt(COL_RS_RESERVATION_ID));

        reservationDetails.setUserId(resultSet.getInt(COL_RS_USER_ID));

        reservationDetails.setUserName(resultSet.getString(COL_NAME));

        reservationDetails.setResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));

        reservationDetails.setResourceName(resultSet.getString(COL_RESOURCE_NAME));

        dateTime = inputFormat.parse(resultSet.getString(COL_START_DATE));
        reservationDetails.setStartDate(date.format(dateTime));

        reservationDetails.setStartTime(time.format(dateTime));

        dateTime = inputFormat.parse(resultSet.getString(COL_END_DATE));
        reservationDetails.setEndDate(date.format(dateTime));

        reservationDetails.setEndTime(time.format(dateTime));
      }
      
    } finally {
      
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }
    return reservationDetails;

  }

  public ReservationDetails findByReservationId(Integer reservationId) throws SQLException, IOException, ParseException {
    ReservationDetails reservationDetails = new ReservationDetails();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
      Date dateTime = null;
      DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
      DateFormat time = new SimpleDateFormat("HH:mm");

      String queryForReservation = "SELECT rs_reservation.rs_reservation_id, " + "rs_reservation.rs_user_id, "
          + "rs_user.name, " + "rs_reservation.rs_resource_id, "
          + "rs_resource.resource_name, " + "rs_reservation.start_date, " + "rs_reservation.end_date "
          + "FROM rs_user, rs_resource, rs_reservation " + "WHERE rs_user.rs_user_id = rs_reservation.rs_user_id AND "
          + "rs_resource.rs_resource_id = rs_reservation.rs_resource_id " + "AND rs_reservation.rs_reservation_id = ?";

      statement = connection.prepareStatement(queryForReservation);

      statement.setObject(1, reservationId);

      resultSet = statement.executeQuery();

      while (resultSet.next()) {

        reservationDetails.setReservationId(resultSet.getInt(COL_RS_RESERVATION_ID));

        reservationDetails.setUserId(resultSet.getInt(COL_RS_USER_ID));

        reservationDetails.setUserName(resultSet.getString(COL_NAME));

        reservationDetails.setResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));

        reservationDetails.setResourceName(resultSet.getString(COL_RESOURCE_NAME));

        dateTime = inputFormat.parse(resultSet.getString(COL_START_DATE));
        reservationDetails.setStartDate(date.format(dateTime));

        reservationDetails.setStartTime(time.format(dateTime));

        dateTime = inputFormat.parse(resultSet.getString(COL_END_DATE));
        reservationDetails.setEndDate(date.format(dateTime));

        reservationDetails.setEndTime(time.format(dateTime));
      }
      
    } finally {
      
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }
    return reservationDetails;
  }

  public List<ReservationDetails> isUsedInDay(LocalDateTime startDateTime, int resourceId, int userId)
      throws SQLException, IOException, ParseException {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    List<ReservationDetails> reservationList = new ArrayList<>();

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      DateFormat inputFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
      Date dateTime = null;
      DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
      DateFormat time = new SimpleDateFormat("HH:mm");

      String queryToFindValidDate = "SELECT * FROM rs_reservation WHERE start_date >= ?"
          + " AND rs_resource_id = ? AND rs_user_id = ?";

      statement = connection.prepareStatement(queryToFindValidDate);

      statement.setString(1, startDateTime.toLocalDate().atStartOfDay().format(formatter));

      statement.setInt(2, resourceId);
      statement.setInt(3, userId);

      resultSet = statement.executeQuery();
      while (resultSet.next()) {
        ReservationDetails reservationDetails = new ReservationDetails();

        reservationDetails.setReservationId(resultSet.getInt(COL_RS_RESERVATION_ID));

        reservationDetails.setUserId(resultSet.getInt(COL_RS_USER_ID));

        reservationDetails.setResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));

        dateTime = inputFormat.parse(resultSet.getString(COL_START_DATE));
        reservationDetails.setStartDate(date.format(dateTime));

        reservationDetails.setStartTime(time.format(dateTime));

        dateTime = inputFormat.parse(resultSet.getString(COL_END_DATE));
        reservationDetails.setEndDate(date.format(dateTime));

        reservationDetails.setEndTime(time.format(dateTime));

        reservationList.add(reservationDetails);
      }
      
    } finally {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }

    return reservationList;
  }
}