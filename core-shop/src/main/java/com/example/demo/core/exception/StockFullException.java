package com.example.demo.core.exception;

public class StockFullException extends IllegalArgumentException {

  public StockFullException(String message) {
    super(message);
  }
}
