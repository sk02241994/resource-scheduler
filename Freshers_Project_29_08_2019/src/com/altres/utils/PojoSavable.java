package com.altres.utils;

import javax.servlet.ServletException;

import com.altres.utils.exception.ValidationServletException;

public interface PojoSavable<T> {

  public void sanitize();

  public void validate(T variable) throws ValidationServletException, ServletException;

  default void sanitizeAndValidate(T variable) throws ValidationServletException, ServletException {
    sanitize();
    validate(variable);
  }
  
  default void sanitizeAndValidate() throws ValidationServletException, ServletException {
    sanitize();
    validate(null);
  }
}
