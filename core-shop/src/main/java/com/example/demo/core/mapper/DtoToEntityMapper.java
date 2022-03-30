package com.example.demo.core.mapper;

import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeId;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeStockChange;

public class DtoToEntityMapper {

  private static Color toEntity(ShoeFilter.Color color) {
      return Color.valueOf(color.name());
  }

  public static ShoeId map(ShoeStockChange shoeStockChange) {
    return ShoeId.builder()
        .size(shoeStockChange.getShoe().getSize())
        .color(toEntity(shoeStockChange.getShoe().getColor()))
        .name(shoeStockChange.getShoe().getName())
        .build();
  }
}
