package com.example.demo.core.mapper;

import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.ShoeStock;

public class EntityToDtoMapper {

  private EntityToDtoMapper()  {
    // disable instantiation of this static class
  }

  public static Shoe toShoeDto(ShoeStockEntity shoe) {
    return Shoe.builder()
        .name(shoe.getName())
        .color(toDto(shoe.getColor()))
        .size(shoe.getSize())
        .build();
  }

  public static ShoeStock toShoeStockDto(ShoeStockEntity stock) {
    return ShoeStock.builder()
        .color(toDto(stock.getColor()))
        .size(stock.getSize())
        .quantity(stock.getQuantity())
        .build();
  }

  private static ShoeFilter.Color toDto(Color color) {
    return ShoeFilter.Color.valueOf(color.name());
  }

}
