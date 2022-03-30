package com.example.demo.dto.out;

import com.example.demo.dto.out.ShopStock.ShopStockBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = ShopStockBuilder.class)
public class ShopStock {
  State state;
  List<ShoeStock> shoes;

  public enum State {
    EMPTY,
    FULL,
    SOME
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class ShopStockBuilder {

  }

}
