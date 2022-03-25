package com.example.demo.core.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.core.AbstractIntegrationTest;
import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ShoeStockRepositoryTest extends AbstractIntegrationTest {

  @Test
  void findBySizeAndColor_nullSizeAndColor_shouldReturnAllShoes() {
    // given
    ShoeStockEntity expectedShoe = shoeStockRepository.save(ShoeStockEntity.builder()
        .color(Color.BLUE)
        .size(BigInteger.ONE)
        .name("shoe 1")
        .build());

    // when
    List<ShoeStockEntity> shoes = shoeStockRepository.findBySizeAndColor(null, null);

    // then
    assertEquals(1, shoes.size());
    assertEquals(expectedShoe, shoes.get(0));
  }

  @Test
  void findBySizeAndColor_sizeAndColorFilter_shouldFilterOnBothCriteria() {
    // given
    ShoeStockEntity expectedShoe = shoeStockRepository.save(ShoeStockEntity.builder()
        .color(Color.BLUE)
        .size(BigInteger.ONE)
        .name("shoe 1")
        .build());
    ShoeStockEntity shoeDistinctColor = shoeStockRepository.save(ShoeStockEntity.builder()
        .color(Color.BLACK)
        .size(BigInteger.ONE)
        .name("shoe 1")
        .build());
    ShoeStockEntity shoeDistinctSize = shoeStockRepository.save(ShoeStockEntity.builder()
        .color(Color.BLUE)
        .size(BigInteger.TWO)
        .name("shoe 1")
        .build());


    // when
    List<ShoeStockEntity> shoes = shoeStockRepository.findBySizeAndColor(
        expectedShoe.getSize(),
        expectedShoe.getColor()
    );

    // then
    assertEquals(1, shoes.size());
    assertEquals(expectedShoe, shoes.get(0));
  }
  
  @Test
  void getTotalQuantity_noData_shouldReturnEmptyOptional() {
    // given

    // when
    Optional<BigInteger> optQuantity = shoeStockRepository.getTotalQuantity();

    // then
    assertTrue(optQuantity.isEmpty());
  }

  @Test
  void getTotalQuantity_twoRows_shouldSumQuantities() {
    // given
    ShoeStockEntity shoe1 = createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.valueOf(14L)
    );
    ShoeStockEntity shoe2 = createStockEntity(
        BigInteger.ONE,
        "Air Jordan 2",
        Color.BLACK,
        BigInteger.valueOf(15L)
    );

    // when
    Optional<BigInteger> optQuantity = shoeStockRepository.getTotalQuantity();

    // then
    assertTrue(optQuantity.isPresent());
    assertEquals(shoe1.getQuantity().add(shoe2.getQuantity()), optQuantity.get());
  }
}
