package com.altres.connection.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class OutLookMailConnectionUtils {

  private static final Logger LOGGER = Logger.getLogger(OutLookMailConnectionUtils.class);
  private static OutLookMailConnectionUtils connectionUtils = null;
  private Properties properties = null;
  private String appId = null;
  private String[] appScope = null;

  private OutLookMailConnectionUtils() {

    try {
      properties = loadPropertyFile();
      this.appId = "9bd3d423-e05f-4b40-8e63-7c34865bf723";
      this.appScope = "User.Read,Mail.Read,Calendars.ReadWrite".split(",");
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

  public static OutLookMailConnectionUtils getInstance() {
    if (connectionUtils == null) {
      connectionUtils = new OutLookMailConnectionUtils();
    }
    return connectionUtils;
  }

  private static Properties loadPropertyFile() throws IOException {
    InputStream inputStream = OutLookMailConnectionUtils.class.getClassLoader().getResourceAsStream("mailAuth.properties");
    Properties properties = new Properties();
    properties.load(inputStream);

    return properties;

  }

  public MailProperties getMailProperties() {
    return new MailProperties(appId, appScope);
  }
}
