package com.altres.mail.util;

import com.altres.connection.util.Authentication;
import com.altres.connection.util.MailProperties;
import com.altres.connection.util.OutLookMailConnectionUtils;
import com.microsoft.graph.models.extensions.User;

public class MailAndCalender {

  public void showUser() {
    OutLookMailConnectionUtils connectionUtils = OutLookMailConnectionUtils.getInstance();
    MailProperties mailProperties = connectionUtils.getMailProperties();
    
    Authentication.initialize(mailProperties.getAppId());
    final String accessToken = Authentication.getUserAccessToken(mailProperties.getAppScopes());
    
    User user = Graph.getUser(accessToken);
    System.out.println("Welcome " + user.displayName);
    System.out.println("Time zone: " + user.mailboxSettings.timeZone);
    System.out.println();
  }
}
