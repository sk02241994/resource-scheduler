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

/**
 * Dao for inserting or updating a user credential.
 */
public class UserDao {

  private static final String COL_USER_ID = "rs_user_id";
  private static final String COL_NAME = "name";
  private static final String COL_EMAIL_ADDRESS = "email_address";
  private static final String COL_IS_ACTIVE = "is_active";
  private static final String COL_IS_ADMIN = "is_admin";
  private static final String COL_PASSWORD = "password";

  /**
   * Method used to save user details.
   * 
   * @param user
   * @throws SQLException
   * @throws IOException
   */
  public void saveUser(User user) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytosave = "INSERT INTO rs_user (name,  email_address, password,"
          + " is_active, created_by, created_date, updated_by,"
          + " updated_date, is_admin) \r\n" + " VALUES (?, ?, ?, ?, ?, now(), ?, now(), ?)";

      statement = connection.prepareStatement(querytosave);

      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail_address());
      statement.setString(3, "user");
      statement.setBoolean(4, user.isEnabled());
      statement.setString(5, user.getName());
      statement.setString(6, user.getName());
      statement.setBoolean(7, user.isAdmin());

      statement.execute();
    } finally {

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
        user.setName(resultSet.getString(COL_NAME));
        user.setEmail_address(resultSet.getString(COL_EMAIL_ADDRESS));
        user.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        user.setIsAdmin(resultSet.getBoolean(COL_IS_ADMIN));
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
   * @param emailAddress
   * @return User
   * @throws SQLException
   * @throws IOException
   */
  public User getSingleUser(String emailAddress) throws SQLException, IOException {

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
   * @throws SQLException
   * @throws IOException
   */
  public void updateUser(User user) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();

      String querytosave = "UPDATE rs_user SET name= ?, email_address = ?,"
          + " is_active = ?, updated_by = ?, updated_date = now(), is_admin = ? WHERE"
          + " email_address = ?";

      statement = connection.prepareStatement(querytosave);

      statement.setString(1, user.getName());
      statement.setString(2, user.getEmail_address());
      statement.setBoolean(3, user.isEnabled());
      statement.setString(4, user.getName());
      statement.setBoolean(5, user.isAdmin());
      statement.setString(6, user.getEmail_address());

      statement.executeUpdate();

    } finally {

      if (statement != null)
        statement.close();

      if (connection != null)
        connection.close();
    }
  }

}
