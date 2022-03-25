package com.example.demo.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.core.AbstractIntegrationTest;
import com.example.demo.core.StockCore;
import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.core.exception.StockFullException;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeKey;
import com.example.demo.dto.in.ShoeStockChange;
import com.example.demo.dto.out.ShopStock;
import com.example.demo.dto.out.ShopStock.State;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StockShopCoreIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private StockCore stockCore;

  @Test
  void getShopStock_emptyData_shouldReturnEmptyState() {

    // when
    ShopStock shopStock = stockCore.getShopStock();

    // then
    assertNotNull(shopStock);
    assertEquals(State.EMPTY, shopStock.getState());
    assertNotNull(shopStock.getShoes());
    assertTrue(shopStock.getShoes().isEmpty());
  }

  @Test
  void getShopStock_shopIsFullWithOneShoe_shouldReturnFullState() {
    // given
    ShoeStockEntity shoe = createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.valueOf(30L)
    );

    // when
    ShopStock shopStock = stockCore.getShopStock();

    // then
    assertNotNull(shopStock);
    assertEquals(State.FULL, shopStock.getState());
    assertNotNull(shopStock.getShoes());
    assertEquals(1, shopStock.getShoes().size());
    assertEquals(shoe.getColor().name(), shopStock.getShoes().get(0).getColor().name());
    assertEquals(shoe.getQuantity(), shopStock.getShoes().get(0).getQuantity());
    assertEquals(shoe.getSize(), shopStock.getShoes().get(0).getSize());
  }

  @Test
  void getShopStock_shopIsFullWithTwoDistinctShoes_shouldReturnFullState() {
    // given
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.valueOf(15L)
    );
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan Number Two",
        Color.BLUE,
        BigInteger.valueOf(15L)
    );

    // when
    ShopStock shopStock = stockCore.getShopStock();

    // then
    assertNotNull(shopStock);
    assertEquals(State.FULL, shopStock.getState());
    assertEquals(2, shopStock.getShoes().size());
  }

  @Test
  void getShopStock_shopIsEmptyWithShoesQuantity0_shouldReturnEmptyStateAndShoes() {
    // given
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.valueOf(0L)
    );
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan Number Two",
        Color.BLUE,
        BigInteger.valueOf(0L)
    );

    // when
    ShopStock shopStock = stockCore.getShopStock();

    // then
    assertNotNull(shopStock);
    assertEquals(State.EMPTY, shopStock.getState());
    assertEquals(2, shopStock.getShoes().size());
  }


  @Test
  void getShopStock_shopIsNotEmptyAndNotFull_shouldReturnSomeState() {
    // given
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.TWO
    );
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan Number TWO",
        Color.BLUE,
        BigInteger.ONE
    );

    // when
    ShopStock shopStock = stockCore.getShopStock();

    // then
    assertNotNull(shopStock);
    assertEquals(State.SOME, shopStock.getState());
    assertEquals(2, shopStock.getShoes().size());
  }

  @Test
  void shoeStockChange_addBoxesToExistingShoeKey_shouldChangeQuantityOfExistingKey() {
    // given
    // one show with quantity 10
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.TEN
    );

    // when
    // change quantity of same shoe key (name, color, size)
    stockCore.shoeStockChange(
        ShoeStockChange.builder()
            .shoe(ShoeKey.builder()
                .color(ShoeFilter.Color.BLACK)
                .size(BigInteger.ONE)
                .name("Air Jordan")
                .build())
            .quantity(BigInteger.valueOf(20L))
            .build()
    );

    // then
    // store should contain one shoe with quantity 20
    ShopStock shopStock = stockCore.getShopStock();
    assertEquals(1, shopStock.getShoes().size());
    assertEquals(BigInteger.valueOf(20L), shopStock.getShoes().get(0).getQuantity());
  }

  @Test
  void shoeStockChange_shoeKeyDoesntExist_shouldCreateIt() {
    // given

    // when
    stockCore.shoeStockChange(
        ShoeStockChange.builder()
            .shoe(ShoeKey.builder()
                .color(ShoeFilter.Color.BLUE)
                .size(BigInteger.ONE)
                .name("Air Jordan")
                .build())
            .quantity(BigInteger.TEN)
            .build()
    );

    // then
    List<ShoeStockEntity> shoes = shoeStockRepository.findAll();
    assertEquals(1, shoes.size());
    assertEquals(Color.BLUE, shoes.get(0).getColor());
    assertEquals("Air Jordan", shoes.get(0).getName());
    assertEquals(BigInteger.ONE, shoes.get(0).getSize());
    assertEquals(BigInteger.TEN, shoes.get(0).getQuantity());
  }

  @Test
  void shoeStockChange_tooMuchShoeBoxes_shouldRaiseStockFullException() {
    // given
    // current stock of 30 shoe boxes
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan",
        Color.BLACK,
        BigInteger.ONE
    );
    createStockEntity(
        BigInteger.ONE,
        "Air Jordan 2",
        Color.BLACK,
        BigInteger.valueOf(29L)
    );

    // when
    // add a shoe boxe
    StockFullException stockFullException = assertThrows(StockFullException.class,
        () -> stockCore.shoeStockChange(
            ShoeStockChange.builder()
                .shoe(ShoeKey.builder()
                    .color(ShoeFilter.Color.BLUE)
                    .size(BigInteger.ONE)
                    .name("Air Jordan")
                    .build())
                .quantity(BigInteger.ONE)
                .build()
        )
    );

    // then
    assertEquals(
        "Stock is full : try to store [31] shoe boxes when maximum allowed is [30]",
        stockFullException.getMessage()
    );
  }

}
