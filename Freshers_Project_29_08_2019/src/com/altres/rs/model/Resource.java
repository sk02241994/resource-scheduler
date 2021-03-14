package com.altres.rs.model;

/**
 * Class that contains getter and setter for resource details.
 */
public class Resource {

  private int rsResourceId;
  private String resourceName;
  private String resourceDescription;
  private boolean isEnabled;
  
  
  public String getResourceDescription() {
    return resourceDescription;
  }
  public void setResourceDescription(String resourceDescription) {
    this.resourceDescription = resourceDescription;
  }
  public int getRsResourceId() {
    return rsResourceId;
  }
  public void setRsResourceId(int rsResourceId) {
    this.rsResourceId = rsResourceId;
  }
  public String getResourceName() {
    return resourceName;
  }
  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }
  public boolean isEnabled() {
    return isEnabled;
  }
  public void setEnabled(boolean isEnabled) {
    this.isEnabled = isEnabled;
  }
  
  
  
}
