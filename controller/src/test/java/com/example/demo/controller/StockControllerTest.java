package com.example.demo.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.core.StockCore;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.in.ShoeKey;
import com.example.demo.dto.in.ShoeStockChange;
import com.example.demo.dto.out.ShoeStock;
import com.example.demo.dto.out.ShopStock;
import com.example.demo.dto.out.ShopStock.State;
import com.example.demo.facade.StockFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(SpringExtension.class)
@WebMvcTest(StockController.class)
class StockControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  StockFacade stockFacade;

  @MockBean
  StockCore stockShopCore;

  @Test
  void getShopStock_goodData_shouldSucceed200() throws Exception {
    // given
    ShopStock stock = ShopStock.builder()
        .state(State.FULL)
        .shoes(
            List.of(
                ShoeStock.builder()
                    .quantity(BigInteger.TWO)
                    .color(Color.BLACK)
                    .size(BigInteger.ONE)
                    .build(),
                ShoeStock.builder()
                    .quantity(BigInteger.TEN)
                    .color(Color.BLUE)
                    .size(BigInteger.ONE)
                    .build()
            )
        )
        .build();
    when(stockFacade.get(0)).thenReturn(stockShopCore);
    when(stockShopCore.getShopStock()).thenReturn(stock);

    // when
    mockMvc.perform(get("/stock").header("version", 0))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.state", is("FULL")))
        .andExpect(jsonPath("$.shoes[0].color", is("BLACK")))
        .andExpect(jsonPath("$.shoes[0].size", is(1)))
        .andExpect(jsonPath("$.shoes[0].quantity", is(2)))
        .andExpect(jsonPath("$.shoes[1].color", is("BLUE")))
        .andExpect(jsonPath("$.shoes[1].size", is(1)))
        .andExpect(jsonPath("$.shoes[1].quantity", is(10)));
  }

  @Test
  void patchShoeStock_invalidNameColorSizeAndQuantity_shouldBe400AndHaveDetailedErrorMessage()
      throws Exception {
    // given
    ShoeKey shoeKey = ShoeKey.builder()
        .name("")
        .color(null)
        .size(BigInteger.valueOf(-1))
        .build();
    ShoeStockChange stockChange = ShoeStockChange.builder()
        .shoe(shoeKey)
        .quantity(BigInteger.valueOf(-2L))
        .build();
    ObjectMapper mapper = new ObjectMapper();
    String jsonBody = mapper.writeValueAsString(stockChange);

    // when
    mockMvc.perform(patch("/stock")
            .header("version", 0)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonBody))
        // then
        .andExpect(status().isBadRequest())
        .andExpect(result ->
            assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
        .andExpect(result ->
            assertTrue(result.getResolvedException().getMessage().contains("with 4 errors")));
  }

}

