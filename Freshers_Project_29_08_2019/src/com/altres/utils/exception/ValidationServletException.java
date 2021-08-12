package com.altres.utils.exception;

import java.util.Collection;

public class ValidationServletException extends Exception {

  private static final long serialVersionUID = 1L;

  private final Collection<String> error;

  public ValidationServletException(Collection<String> error) {
    super("Validation exception thrown");
    this.error = error;
  }

  public Collection<String> getError() {
    return this.error;
  }

}
