package com.altres.connection.util;

public class MailProperties {

  private String appId;
  private String[] appScopes;

  public MailProperties(String appId, String[] appScopes) {
    this.appId = appId;
    this.appScopes = appScopes;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String[] getAppScopes() {
    return appScopes;
  }

  public void setAppScopes(String[] appScopes) {
    this.appScopes = appScopes;
  }

}
