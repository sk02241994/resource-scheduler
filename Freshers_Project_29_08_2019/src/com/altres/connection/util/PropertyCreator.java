package com.altres.connection.util;

import java.io.IOException;
import java.io.InputStream;

public class PropertyCreator<E> implements PropertyLoadInterface<E> {

  private E type;

  @Override
  public E getInstance(Class<E> clazz) {
    try {
      type = clazz.newInstance();
      return type;
    } catch (InstantiationException | IllegalAccessException e) {
      return null;
    }
  }

  @Override
  public void loadPropertyFile(String propertyFileName) throws IOException {
    InputStream inputStream = this.type.getClass().getClassLoader().getResourceAsStream(propertyFileName);
    properties.load(inputStream);
  }
}
