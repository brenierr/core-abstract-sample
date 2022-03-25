package com.example.demo.facade;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFacade<T> {
  private final Map<Integer, T> implementations = new HashMap<>();

  public T get(Integer version) {
    T implementation = implementations.get(version);
    if (implementation != null) {
      return implementation;
    }
    throw new IllegalArgumentException(
        "version " + version + " is not registered as a ShoeCore implementation"
    );
  }

  public void register(Integer version, T implementation) {
    if (implementations.containsKey(version)) {
      throw new IllegalArgumentException("ShoeCore implementations already contains an "
          + "implementation with version " + version);
    }
    this.implementations.put(version, implementation);
  }
}
