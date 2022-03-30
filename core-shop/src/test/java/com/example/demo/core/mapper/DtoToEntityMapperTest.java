package com.example.demo.core.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.demo.core.domain.ShoeId;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.in.ShoeKey;
import com.example.demo.dto.in.ShoeStockChange;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

class DtoToEntityMapperTest {

  @Test
  void map_goodData_shouldMapAllFields() {
    // given
    ShoeStockChange shoeStockChange = ShoeStockChange.builder()
        .quantity(BigInteger.ONE)
        .shoe(ShoeKey.builder()
            .size(BigInteger.TEN)
            .color(Color.BLACK)
            .name("Decathlon shoe 1")
            .build())
        .build();

    // when
    ShoeId shoeId = DtoToEntityMapper.map(shoeStockChange);

    // then
    assertEquals(
        shoeStockChange.getShoe().getColor().name(),
        shoeId.getColor().name()
    );
    assertEquals(
        shoeStockChange.getShoe().getSize(),
        shoeId.getSize()
    );
    assertEquals(
        shoeStockChange.getShoe().getName(),
        shoeId.getName()
    );
  }
}
