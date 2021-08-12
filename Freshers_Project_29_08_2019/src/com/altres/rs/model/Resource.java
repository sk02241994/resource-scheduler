package com.altres.rs.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.altres.rs.dao.ReservationDao;
import com.altres.utils.PojoDeletable;
import com.altres.utils.PojoSavable;
import com.altres.utils.exception.ValidationServletException;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Class that contains getter and setter for resource details.
 */
public class Resource implements PojoSavable<Void>, PojoDeletable<Integer> {

  private Integer rsResourceId;
  private String resourceName;
  private String resourceDescription;
  private boolean isEnabled;
  private Integer timeLimit;
  private boolean isAllowedMultiple;
  private String timeLimitHours;
  private String timeLimitMinutes;

  public String getResourceDescription() {
    return resourceDescription;
  }

  public void setResourceDescription(String resourceDescription) {
    this.resourceDescription = resourceDescription;
  }

  public Integer getRsResourceId() {
    return rsResourceId;
  }

  public void setRsResourceId(Integer rsResourceId) {
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

  public void setTimeLimit(Integer timeLimit) {
    this.timeLimit = timeLimit;
  }

  public Integer getTimeLimit() {
    return this.timeLimit;
  }

  public void setIsAllowedMultiple(boolean isAllowedMultiple) {
    this.isAllowedMultiple = isAllowedMultiple;
  }

  public boolean isAllowedMultiple() {
    return this.isAllowedMultiple;
  }

  public void setTimeLimitHours(String timeLimitHours) {
    this.timeLimitHours = timeLimitHours;
  }

  public String getTimeLimitHours() {
    return this.timeLimitHours;
  }

  public void setTimeLimitMinutes(String timeLimitMinutes) {
    this.timeLimitMinutes = timeLimitMinutes;
  }

  public String getTimeLimitMinutes() {
    return this.timeLimitMinutes;
  }

  @Override
  public void sanitize() {
    setResourceName(StringUtils.trimToNull(getResourceName()));
    setResourceDescription(StringUtils.trimToNull(getResourceDescription()));

  }

  @Override
  public void validate(Void variable) throws ValidationServletException {
    List<String> errors = new ArrayList<>();
    
    if(StringUtils.isBlank(getResourceName())) {
      errors.add("Resource name cannot be empty");
    }
    
    if((StringUtils.isNotBlank(getTimeLimitHours())
        && !NumberUtils.isCreatable(getTimeLimitHours()))
        || (StringUtils.isNotBlank(getTimeLimitMinutes())
            && !NumberUtils.isCreatable(getTimeLimitMinutes()))) {
      errors.add("Please enter valid hours and minutes.");
    }
    
    if (!errors.isEmpty()) {
      throw new ValidationServletException(errors);
    }

  }

  @Override
  public void validateDelete(Integer variable) throws ValidationServletException {
    ReservationDao reservationDao = new ReservationDao();
    try {
      List<Reservation> reservations = reservationDao.findByResourceId(variable);
      if(!reservations.isEmpty()) {
        throw new ValidationServletException(Arrays.asList("Unable to delete as the resource is already in use."));
      }
    } catch (SQLException | IOException | ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
}
