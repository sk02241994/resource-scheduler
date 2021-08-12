package com.altres.utils;

import com.altres.utils.exception.ValidationServletException;

public interface PojoDeletable<T> {

  public void validateDelete(T variable) throws ValidationServletException; 
}
