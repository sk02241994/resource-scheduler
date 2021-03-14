package com.altres.connection.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class for establishing connection with sql database
 */
public class SqlConnection {
  
  private static SqlConnection sqlConnection = null ;
  private Properties properties = null;
  private String dbUrl = null;
  private String username = null;
  private String password = null;
  
  
  private SqlConnection() throws SQLException, IOException{
    DriverManager.registerDriver(new com.mysql.jdbc.Driver());
    
      properties = loadProepertyFile();
      dbUrl = properties.getProperty("MYSQLJDBC.url");
      username = properties.getProperty("MYSQLJDBC.username");
      password = properties.getProperty("MYSQLJDBC.password");
   
  }
  
  /**
   * Singleton design
   * 
   * @return SqlConnection
   * @throws SQLException 
   * @throws IOException 
   */
  public static SqlConnection getInstance() throws SQLException, IOException{
    if(sqlConnection == null) {
      sqlConnection = new SqlConnection();
    }
    return sqlConnection;
  }
    
  /**
   * used to read from property file and get the properties
   * 
   * @return Properties
   * @throws IOException
   */
  private static Properties loadProepertyFile() throws IOException {
    InputStream inputStream = SqlConnection.class.getClassLoader().getResourceAsStream("jdbc.properties"); 
    Properties properties = new Properties();
    properties.load(inputStream);
    
    return properties;
    
  }
 
  /**
   * Used to initialize the connection to the database
   * using the components from properties file
   * 
   * @return Connection
   * @throws SQLException
   */
  public Connection initalizeConnection() throws SQLException {
    return DriverManager.getConnection(dbUrl, username, password);

  }

}
