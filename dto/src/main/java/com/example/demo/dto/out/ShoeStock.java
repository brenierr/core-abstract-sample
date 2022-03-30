package com.example.demo.dto.out;

import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.ShoeStock.ShoeStockBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ShoeStockBuilder.class)
public class ShoeStock {

  BigInteger size;
  Color      color;
  BigInteger quantity;


  @JsonPOJOBuilder(withPrefix = "")
  public static class ShoeStockBuilder {

  }

}
