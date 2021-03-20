package com.altres.mail.util;

import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.User;
import com.microsoft.graph.requests.extensions.GraphServiceClient;

public class Mail {

  public void test() {
    
    SimpleAuthProvider authProvider = new SimpleAuthProvider("");
    
    IGraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider( authProvider ).buildClient();

    User user = graphClient.me()
      .buildRequest()
      .get();
  }
}
