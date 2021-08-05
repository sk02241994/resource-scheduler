package com.altres.rs.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.altres.connection.util.SqlConnection;
import com.altres.rs.model.User;

/**
 * Dao for handling all the requests coming for logging in and changing password.
 */
public class LoginDao {

  private static final String COL_RS_USER_ID = "rs_user_id";
  private static final String COL_NAME = "name";
  private static final String COL_EMAIL_ADDRESS = "email_address";
  private static final String COL_PASSWORD = "password";
  private static final String COL_IS_ACTIVE = "is_active";
  private static final String COL_IS_ADMIN = "is_admin";

  /**
   * Method to get all the user details if the email address and password is correct.
   * Those will be used for validating.
   * 
   * @param emailAddress
   * @param password
   * @return
   * @throws SQLException
   * @throws IOException
   */
  public User getUser(String emailAddress, String password) throws SQLException, IOException {

    User user = new User();
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    try {
      connection = SqlConnection.getInstance().initalizeConnection();
      String querytogetusers = "SELECT * FROM rs_user WHERE email_address = ? AND password = ?";

      statement = connection.prepareStatement(querytogetusers);
      statement.setString(1, emailAddress);
      statement.setString(2, password);

      resultSet = statement.executeQuery();

      if (resultSet.next()) {
        user.setRsUserId(resultSet.getInt(COL_RS_USER_ID));
        user.setName(resultSet.getString(COL_NAME));
        user.setEmail_address(resultSet.getString(COL_EMAIL_ADDRESS));
        user.setPassword(resultSet.getString(COL_PASSWORD));
        user.setEnabled(resultSet.getBoolean(COL_IS_ACTIVE));
        user.setIsAdmin(resultSet.getBoolean(COL_IS_ADMIN));
      }
      
    }finally {
      
      if(statement != null)
        statement.close();
      
      if(resultSet != null)
        resultSet.close();
      
    }

    return user;
  }

  /**
   * Method used for changing password if the user is logging in for first time.
   * 
   * @param emailAddress
   * @param newpassword
   * @throws SQLException
   * @throws IOException
   */
  public void changePassword(String emailAddress, String newpassword) throws SQLException, IOException {

    Connection connection = null;
    PreparedStatement statement = null;

    try {
    connection = SqlConnection.getInstance().initalizeConnection();
    String updateQuery = "update rs_user set password = ? where email_address = ?";
    statement = connection.prepareStatement(updateQuery);

    statement.setString(1, newpassword);
    statement.setString(2, emailAddress);

    statement.executeUpdate();
    }finally {
      if(statement != null)
        statement.close();
    }
  }

}
