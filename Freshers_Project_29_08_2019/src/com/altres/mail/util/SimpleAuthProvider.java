package com.altres.mail.util;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

public class SimpleAuthProvider implements IAuthenticationProvider{

  private String accessToken = null;
  
  public SimpleAuthProvider(String accessToken) {
    this.accessToken = accessToken;
  }
  
  @Override
  public void authenticateRequest(IHttpRequest request) {
    request.addHeader("Authorization", "Bearer " + accessToken);
  }

}
