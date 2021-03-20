package com.altres.connection.util;

import java.io.IOException;
import java.util.Properties;

public interface PropertyLoadInterface<E> {

  Properties properties = new Properties();
  public void loadPropertyFile(String propertyFileName) throws IOException;

  default E getInstance(Class<E> clazz) {
    return null;
  }
}
