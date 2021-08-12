package com.altres.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

public interface NoticeInterface {

  List<String> errors = new ArrayList<>();
  List<String> success = new ArrayList<>();
  
  List<String> modalErrors = new ArrayList<>();
  List<String> modalSuccess = new ArrayList<>();

  default void addErrorNotice(String message) {
    errors.add(message);
  }

  default void addErrorNotice(Collection<String> messages) {
    errors.addAll(messages);
  }

  default void addSuccessNotice(String message) {
    success.add(message);
  }

  default void clearNotices() {
    errors.clear();
    success.clear();
    modalErrors.clear();
    modalSuccess.clear();
  }
  
  default void displayNotice(HttpServletRequest request) {
    request.setAttribute("error_message", errors);
    request.setAttribute("success_message", success);
    displayModalNotice(request);
  }
  
  default void addModalErrorNotice(String message) {
    modalErrors.add(message);
  }

  default void addModalErrorNotice(Collection<String> messages) {
    modalErrors.addAll(messages);
  }

  default void addModalSuccessNotice(String message) {
    modalSuccess.add(message);
  }
  
  default void displayModalNotice(HttpServletRequest request) {
    Gson errorGson = new Gson();
    Gson successGson = new Gson();
    request.setAttribute("error_message_modal", errorGson.toJson(modalErrors));
    request.setAttribute("successs_message_modal", successGson.toJson(modalSuccess));
  }
}
