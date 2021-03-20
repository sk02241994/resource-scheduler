package com.altres.connection.util;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import org.apache.log4j.Logger;

import com.microsoft.aad.msal4j.DeviceCode;
import com.microsoft.aad.msal4j.DeviceCodeFlowParameters;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;

public class Authentication {

  private static final Logger LOGGER = Logger.getLogger(Authentication.class);
  private static String applicationId;
  // Set authority to allow only organizational accounts
  // Device code flow only supports organizational accounts
  private final static String tenetId = "f8cdef31-a31e-4b4a-93e4-5f571e91255a";
  private final static String authority = "https://login.microsoftonline.com/"+tenetId+"/oauth2/v2.0/authorize?";

  public static void initialize(String applicationId) {
    Authentication.applicationId = applicationId;
  }

  public static String getUserAccessToken(String[] scopes) {
    if (applicationId == null) {
      return "You must initialize Authentication before calling getUserAccessToken";
    }

    Set<String> scopeSet = new HashSet<>(Arrays.asList(scopes));

    ExecutorService pool = Executors.newFixedThreadPool(1);
    PublicClientApplication app = null;
    try {
      // Build the MSAL application object with
      // app ID and authority
      app = PublicClientApplication.builder(applicationId)
          .authority(authority)
          .executorService(pool)
          .build();
    } catch (MalformedURLException e) {
      LOGGER.error(e.getMessage());
    }

    // Create consumer to receive the DeviceCode object
    // This method gets executed during the flow and provides
    // the URL the user logs into and the device code to enter
    Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
      // Print the login information to the console
      System.out.println(deviceCode.message());
    };

    // Request a token, passing the requested permission scopes
    IAuthenticationResult result = app
        .acquireToken(DeviceCodeFlowParameters.builder(scopeSet, deviceCodeConsumer).build())
        .exceptionally(ex -> {
          LOGGER.error("Unable to authenticate - " + ex.getMessage());
          return null;
        })
        .join();

    pool.shutdown();

    if (result != null) {
      return result.accessToken();
    }

    return null;
  }
}
