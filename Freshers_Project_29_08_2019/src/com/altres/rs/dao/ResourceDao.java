package com.altres.rs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.altres.connection.util.SqlConnection;
import com.altres.rs.model.Resource;
import com.mysql.jdbc.Statement;

/**
 * Dao for inserting a resource, updating a resource or deleting a resource and is used to display all the available resource
 * from the database or get single resource for editing.
 */
public class ResourceDao {

  public static final String COL_RS_RESOURCE_ID = "rs_resource_id";
  public static final String COL_RESOURCE_NAME = "resource_name";
  public static final String COL_IS_ACTIVE = "is_active";
  public static final String COL_DESCRIPTION = "description";
  public static final String COL_CREATED_BY = "created_by";
  public static final String COL_CREATED_DATE = "created_date";
  public static final String COL_UPDATED_BY = "updated_by";
  public static final String COL_UPDATED_DATE = "updated_date";
  public static final String COL_TIME_LIMIT = "time_limit";
  public static final String COL_IS_ALLOWED_MULTIPLE = "is_allowed_multiple";
  public static final String COL_MAX_USER_BOOKINGS = "max_user_bookings";
  public static final String COL_IS_PERMANENT_EMPLOYEE = "is_permanent_employee";
  /**
   * Method to save the resource in database.
   * 
   * @param resource
   * @return 
   * @throws SQLException
   * @throws IOException
   */
  private Integer saveResource(Resource resource) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet generatedKeys = null;
    Integer generatedId = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytosave = "INSERT INTO rs_resource"
          + " (resource_name, is_active, description, created_by, created_date, updated_by, updated_date, time_limit,"
          + " is_allowed_multiple, max_user_bookings, is_permanent_employee)"
          + " VALUES(?, ?, ?, ?, now(), ?, now(), ?, ?, ?, ?)";
      statement = connection.prepareStatement(querytosave, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, resource.getResourceName());
      statement.setBoolean(2, resource.isEnabled());
      statement.setString(3, resource.getResourceDescription());
      statement.setString(4, resource.getResourceName());
      statement.setString(5, resource.getResourceName());
      statement.setObject(6, resource.getTimeLimit());
      statement.setBoolean(7, resource.isAllowedMultiple());
      statement.setObject(8, resource.getMaxUserBooking());
      statement.setBoolean(9, resource.isPermanentEmployee());

      statement.execute();
      
      generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        generatedId = generatedKeys.getInt(1);
      }
      return generatedId;
    } finally {

      if (generatedKeys != null) {
        generatedKeys.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connection != null) {
        connection.close();
      }
    }

  }

  /**
   * Method used to get all resources from the database.
   * 
   * @return List<Resource>
   * @throws SQLException
   * @throws IOException
   */
  public List<Resource> getResource() throws SQLException, IOException {

    List<Resource> resourcelist = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytogetresource = "SELECT * FROM rs_resource";

      statement = connection.prepareStatement(querytogetresource);
      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Resource resource = new Resource();
        resource.setRsResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));
        resource.setResourceName(resultSet.getString(COL_RESOURCE_NAME));
        resource.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        resource.setResourceDescription(resultSet.getString(COL_DESCRIPTION));
        resource.setTimeLimit(resultSet.getInt(COL_TIME_LIMIT));
        resource.setIsAllowedMultiple(resultSet.getBoolean(COL_IS_ALLOWED_MULTIPLE));
        resource.setMaxUserBooking(resultSet.getInt(COL_MAX_USER_BOOKINGS));
        resource.setIsPermanentEmployee(resultSet.getBoolean(COL_IS_PERMANENT_EMPLOYEE));
        resourcelist.add(resource);
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

    return resourcelist;
  }

  /**
   * Method used to get a single resource to be displayed for editing.
   * 
   * @param resouceid
   * @return Resource
   * @throws SQLException
   * @throws IOException
   */
  public Resource getSingleResource(int resouceid) throws SQLException, IOException {

    Resource resource = new Resource();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytogetresource = "SELECT * FROM rs_resource WHERE rs_resource_id = ?";
      statement = connection.prepareStatement(querytogetresource);
      statement.setInt(1, resouceid);

      resultSet = statement.executeQuery();

      if (resultSet.next()) {
        resource.setRsResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));
        resource.setResourceName(resultSet.getString(COL_RESOURCE_NAME));
        resource.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        resource.setResourceDescription(resultSet.getString(COL_DESCRIPTION));
        resource.setTimeLimit(resultSet.getInt(COL_TIME_LIMIT));
        resource.setIsAllowedMultiple(resultSet.getBoolean(COL_IS_ALLOWED_MULTIPLE));
        resource.setMaxUserBooking((Integer)resultSet.getObject(COL_MAX_USER_BOOKINGS));
        resource.setIsPermanentEmployee(resultSet.getBoolean(COL_IS_PERMANENT_EMPLOYEE));
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

    return resource;
  }

  /**
   * Method used to update details or a resource.
   * 
   * @param resource
   * @throws SQLException
   * @throws IOException
   */
  private void updateResource(Resource resource) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytoupdate = "UPDATE rs_resource SET resource_name = ?, is_active = ?, description = ?, time_limit = ?, "
          + "is_allowed_multiple = ?, max_user_bookings = ?, is_permanent_employee = ? WHERE rs_resource_id = ?";

      statement = connection.prepareStatement(querytoupdate);

      statement.setString(1, resource.getResourceName());
      statement.setBoolean(2, resource.isEnabled());
      statement.setString(3, resource.getResourceDescription());
      statement.setObject(4, resource.getTimeLimit());
      statement.setObject(5, resource.isAllowedMultiple());
      statement.setObject(6, resource.getMaxUserBooking());
      statement.setBoolean(7, resource.isPermanentEmployee());
      statement.setInt(8, resource.getRsResourceId());

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
   * Method used to delete a selected resource.
   * 
   * @param id
   * @throws SQLException
   * @throws IOException
   */
  public void deleteResource(int id) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String queryToDelete = "DELETE FROM rs_resource WHERE rs_resource_id = ?";
      statement = connection.prepareStatement(queryToDelete);

      statement.setInt(1, id);

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
   * Method used to get resource.
   * 
   * @return List<Resource>
   * @throws SQLException
   * @throws IOException
   */
  public List<Resource> getResourceForUser() throws SQLException, IOException {

    List<Resource> resourcelist = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytogetresource = "SELECT * FROM rs_resource WHERE is_active = 1";

      statement = connection.prepareStatement(querytogetresource);
      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        Resource resource = new Resource();
        resource.setRsResourceId(resultSet.getInt(COL_RS_RESOURCE_ID));
        resource.setResourceName(resultSet.getString(COL_RESOURCE_NAME));
        resource.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        resource.setResourceDescription(resultSet.getString(COL_DESCRIPTION));
        resourcelist.add(resource);
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
    return resourcelist;
  }

  public Integer upsert(Resource resource) throws SQLException, IOException {
    Integer resourceId = resource.getRsResourceId();
    if(resource.getRsResourceId() != null) {
      updateResource(resource);
    } else {
      resourceId = saveResource(resource);
    }
    return resourceId;
  }
}
