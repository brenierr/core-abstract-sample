package com.example.demo.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.core.AbstractIntegrationTest;
import com.example.demo.core.ShoeCore;
import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ShoeShopCoreIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ShoeCore shoeCore;

  @Test
  void search_shoeFilterWithNullParameters_shouldReturnAllShoes() {
    // given
    ShoeStockEntity blackShoeSizeOne = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLACK)
            .name("Air jordan 1")
            .size(BigInteger.ONE)
            .build()
    );
    ShoeStockEntity blackShoeSizeTwo = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLACK)
            .name("Air jordan 2")
            .size(BigInteger.TWO)
            .build()
    );
    ShoeStockEntity blueShoeSizeTwo = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLUE)
            .name("Air jordan 3")
            .size(BigInteger.TWO)
            .build()
    );

    // when
    Shoes shoes = shoeCore.search(new ShoeFilter(null, null));

    // then
    assertNotNull(shoes);
    assertNotNull(shoes.getShoes());
    assertEquals(3, shoes.getShoes().size());
    List<String> expectedShoeNames = Arrays.asList(
        blackShoeSizeOne.getName(),
        blackShoeSizeTwo.getName(),
        blueShoeSizeTwo.getName()
    );
    assertTrue(
        expectedShoeNames.containsAll(
            shoes.getShoes().stream()
                .map(Shoe::getName)
                .collect(Collectors.toList())
        )
    );
  }

  @Test
  void search_shoeFilterWithAllFilters_shouldReturnMatchingShoes() {
    // given
    ShoeStockEntity blackShoeSizeOne = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLACK)
            .name("Air jordan")
            .size(BigInteger.ONE)
            .build()
    );
    ShoeStockEntity blackShoeSizeTwo = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLACK)
            .name("Air jordan")
            .size(BigInteger.TWO)
            .build()
    );
    ShoeStockEntity blueShoeSizeOne = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLUE)
            .name("Air jordan")
            .size(BigInteger.ONE)
            .build()
    );
    ShoeStockEntity blueShoeSizeTwo = shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(Color.BLUE)
            .name("Air jordan")
            .size(BigInteger.TWO)
            .build()
    );

    // when
    Shoes blueShoesSizeTwoResult = shoeCore.search(
        new ShoeFilter(BigInteger.TWO, ShoeFilter.Color.BLUE)
    );

    // then
    assertNotNull(blueShoesSizeTwoResult);
    assertNotNull(blueShoesSizeTwoResult.getShoes());
    assertEquals(1, blueShoesSizeTwoResult.getShoes().size());
    assertEquals(
        blueShoeSizeTwo.getName(),
        blueShoesSizeTwoResult.getShoes().get(0).getName()
    );
    assertEquals(
        blueShoeSizeTwo.getSize(),
        blueShoesSizeTwoResult.getShoes().get(0).getSize()
    );
    assertEquals(
        blueShoeSizeTwo.getColor().name(),
        blueShoesSizeTwoResult.getShoes().get(0).getColor().name()
    );
  }

}
