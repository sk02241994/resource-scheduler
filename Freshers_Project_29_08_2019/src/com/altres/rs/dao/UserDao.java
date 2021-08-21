package com.altres.rs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.altres.connection.util.SqlConnection;
import com.altres.rs.model.User;
import com.altres.utils.Gender;
import com.mysql.jdbc.Statement;

/**
 * Dao for inserting or updating a user credential.
 */
public class UserDao {

  public static final String COL_USER_ID = "rs_user_id";
  public static final String COL_NAME = "name";
  public static final String COL_EMAIL_ADDRESS = "email_address";
  public static final String COL_IS_ACTIVE = "is_active";
  public static final String COL_IS_ADMIN = "is_admin";
  public static final String COL_PASSWORD = "password";
  public static final String COL_GENDER = "gender";
  public static final String COL_IS_PERMANENT_EMPLOYEE = "is_permanent_employee";

  /**
   * Method used to save user details.
   * 
   * @param user
   * @throws SQLException
   * @throws IOException
   */
  private Integer saveUser(User user) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet generatedKeys = null;
    Integer generatedId = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytosave = "INSERT INTO rs_user (name,  email_address, password,"
          + " is_active, created_by, created_date, updated_by,"
          + " updated_date, is_admin, gender, is_permanent_employee) \r\n"
          + " VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?, ?, ?)";

      statement = connection.prepareStatement(querytosave, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail_address());
      statement.setString(3, "user");
      statement.setBoolean(4, user.isEnabled());
      statement.setString(5, user.getName());
      statement.setString(6, user.getName());
      statement.setBoolean(7, user.isAdmin());
      statement.setString(8, user.getGender().toString());
      statement.setBoolean(9, user.isPermanentEmployee());

      statement.executeUpdate();

      generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        generatedId = generatedKeys.getInt(1);
      }
      return generatedId;
    } finally {

      if (generatedKeys != null) {
        generatedKeys.close();
      }

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
  }

  /**
   * Method used to get all user details.
   * 
   * @return List<User>
   * @throws SQLException
   * @throws IOException
   */
  public List<User> getUser() throws SQLException, IOException {

    List<User> userlist = new ArrayList<>();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      String querytogetusers = "SELECT * FROM rs_user";

      statement = connection.prepareStatement(querytogetusers);
      resultSet = statement.executeQuery();

      while (resultSet.next()) {
        User user = new User();
        user.setRsUserId(resultSet.getInt(COL_USER_ID));
        user.setName(resultSet.getString(COL_NAME));
        user.setEmail_address(resultSet.getString(COL_EMAIL_ADDRESS));
        user.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        user.setIsAdmin(resultSet.getBoolean(COL_IS_ADMIN));
        user.setGender(Gender.valueOf(resultSet.getString(COL_GENDER)));
        user.setIsPermanentEmployee(resultSet.getBoolean(COL_IS_PERMANENT_EMPLOYEE));
        userlist.add(user);
      }
    } finally {

      if (resultSet != null)
        resultSet.close();

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
    return userlist;
  }

  /**
   * Method used to get a single user details to be display.
   * 
   * @param userId
   * @return User
   * @throws SQLException
   * @throws IOException
   */
  public User getSingleUser(String userId) throws SQLException, IOException {

    User user = new User();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      String querytogetusers = "SELECT * FROM rs_user WHERE rs_user_id = ?";

      statement = connection.prepareStatement(querytogetusers);
      statement.setString(1, userId);

      resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user.setRsUserId(resultSet.getInt(COL_USER_ID));
        user.setName(resultSet.getString(COL_NAME));
        user.setEmail_address(resultSet.getString(COL_EMAIL_ADDRESS));
        user.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        user.setIsAdmin(resultSet.getBoolean(COL_IS_ADMIN));
        user.setPassword(resultSet.getString(COL_PASSWORD));
        user.setGender(Gender.valueOf(resultSet.getString(COL_GENDER)));
        user.setIsPermanentEmployee(resultSet.getBoolean(COL_IS_PERMANENT_EMPLOYEE));
      }
    } finally {

      if (resultSet != null)
        resultSet.close();

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
    return user;
  }

  /**
   * Method to update a users details.
   * 
   * @param user
   * @return
   * @throws SQLException
   * @throws IOException
   */
  private Integer updateUser(User user) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet generatedKeys = null;
    Integer generatedId = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytosave = "UPDATE rs_user SET name= ?, email_address = ?,"
          + " is_active = ?, updated_by = ?, updated_date = now(), is_admin = ?, "
          + " gender = ?, is_permanent_employee = ?"
          + " WHERE rs_user_id = ?";

      statement = connection.prepareStatement(querytosave, Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail_address());
      statement.setBoolean(3, user.isEnabled());
      statement.setString(4, user.getName());
      statement.setBoolean(5, user.isAdmin());
      statement.setString(6, user.getGender().toString());
      statement.setBoolean(7, user.isPermanentEmployee());
      statement.setObject(8, user.getRsUserId());

      statement.executeUpdate();

      generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        generatedId = generatedKeys.getInt(1);
      }
      return generatedId;

    } finally {

      if (generatedKeys != null) {
        generatedKeys.close();
      }

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
  }

  public Integer upsert(User user) throws SQLException, IOException {
    Integer userId = user.getRsUserId();
    if(userId != null) {
      updateUser(user);
    } else {
      userId = saveUser(user);
    }
    return userId;
  }

  /**
   * Method used to get a single user details to be display.
   * 
   * @param emailAddress
   * @return User
   * @throws SQLException
   * @throws IOException
   */
  public User getUserByEmail(String emailAddress) throws SQLException, IOException {

    User user = new User();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      String querytogetusers = "SELECT * FROM rs_user WHERE email_address = ?";

      statement = connection.prepareStatement(querytogetusers);
      statement.setString(1, emailAddress);

      resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user.setRsUserId(resultSet.getInt(COL_USER_ID));
        user.setName(resultSet.getString(COL_NAME));
        user.setEmail_address(resultSet.getString(COL_EMAIL_ADDRESS));
        user.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        user.setIsAdmin(resultSet.getBoolean(COL_IS_ADMIN));
        user.setPassword(resultSet.getString(COL_PASSWORD));
        user.setGender(Gender.valueOf(resultSet.getString(COL_GENDER)));
        user.setIsPermanentEmployee(resultSet.getBoolean(COL_IS_PERMANENT_EMPLOYEE));
      }
    } finally {

      if (resultSet != null)
        resultSet.close();

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
    return user;
  }
}
