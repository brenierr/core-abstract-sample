package com.example.demo.core.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.ShoeStock;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class EntityToDtoMapperTest {

  @Test
  void toShoeStockDto_goodData_shouldMap() {
    // given
    ShoeStockEntity stockEntity = ShoeStockEntity.builder()
        .name("Decathlon 1")
        .color(Color.BLACK)
        .size(BigInteger.ONE)
        .quantity(BigInteger.TWO)
        .build();

    // when
    ShoeStock dto = EntityToDtoMapper.toShoeStockDto(stockEntity);

    // then
    assertEquals(stockEntity.getSize(), dto.getSize());
    assertEquals(stockEntity.getColor().name(), dto.getColor().name());
    assertEquals(stockEntity.getQuantity(), dto.getQuantity());
  }

  @Test
  void toShoeDto_goodData_shouldMap() {
    // given
    ShoeStockEntity shoe = ShoeStockEntity.builder()
        .name("Decathlon 1")
        .color(Color.BLACK)
        .size(BigInteger.ONE)
        .build();

    // when
    Shoe dto = EntityToDtoMapper.toShoeDto(shoe);

    // then
    assertEquals(shoe.getSize(), dto.getSize());
    assertEquals(shoe.getColor().name(), dto.getColor().name());
    assertEquals(shoe.getName(), dto.getName());
  }
}
