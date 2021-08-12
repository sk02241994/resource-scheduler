package com.altres.utils;

import javax.servlet.http.HttpServletRequest;

public interface FormInterface <T>{
  T getForm (HttpServletRequest request);
}
