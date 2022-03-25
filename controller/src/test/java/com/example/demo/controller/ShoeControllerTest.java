package com.example.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.core.ShoeCore;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.in.ShoeFilter.Color;
import com.example.demo.dto.out.Shoe;
import com.example.demo.dto.out.Shoes;
import com.example.demo.facade.ShoeFacade;
import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ShoeController.class)
class ShoeControllerTest {


  @Autowired
  MockMvc mockMvc;

  @MockBean
  ShoeFacade shoeFacade;

  @MockBean
  ShoeCore shoeCore;

  @Test
  void search_goodDataNoFilter_shouldSucceed200() throws Exception {
    // given
    Shoes shoes =
        Shoes.builder()
            .shoes(
                List.of(
                    Shoe.builder().size(BigInteger.ONE).color(Color.BLACK).name("Shoe 1").build(),
                    Shoe.builder().size(BigInteger.TWO).color(Color.BLUE).name("Shoe 2").build()
                )
            )
            .build();
    when(shoeFacade.get(0)).thenReturn(shoeCore);
    when(shoeCore.search(any())).thenReturn(shoes);

    // when
    mockMvc.perform(get("/shoes/search")
            .header("version", 0))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shoes", hasSize(2)))
        .andExpect(jsonPath("$.shoes[0].color", is("BLACK")))
        .andExpect(jsonPath("$.shoes[0].size", is(1)))
        .andExpect(jsonPath("$.shoes[0].name", is("Shoe 1")))
        .andExpect(jsonPath("$.shoes[1].color", is("BLUE")))
        .andExpect(jsonPath("$.shoes[1].size", is(2)))
        .andExpect(jsonPath("$.shoes[1].name", is("Shoe 2")));
  }

  @Test
  void search_goodDataWithFilter_shouldSucceed200() throws Exception {
    // given
    ShoeFilter shoeFilter = new ShoeFilter(BigInteger.ONE, Color.BLACK);
    Shoes shoes =
        Shoes.builder()
            .shoes(
                List.of(
                    Shoe.builder().size(BigInteger.ONE).color(Color.BLACK).name("Shoe 1").build(),
                    Shoe.builder().size(BigInteger.TWO).color(Color.BLUE).name("Shoe 2").build()
                )
            )
            .build();
    when(shoeFacade.get(0)).thenReturn(shoeCore);
    when(shoeCore.search(shoeFilter)).thenReturn(shoes);

    // when
    mockMvc.perform(get("/shoes/search")
            .header("version", 0)
            .param("size", "1")
            .param("color", "BLACK"))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.shoes", hasSize(2)))
        .andExpect(jsonPath("$.shoes[0].color", is("BLACK")))
        .andExpect(jsonPath("$.shoes[0].size", is(1)))
        .andExpect(jsonPath("$.shoes[0].name", is("Shoe 1")))
        .andExpect(jsonPath("$.shoes[1].color", is("BLUE")))
        .andExpect(jsonPath("$.shoes[1].size", is(2)))
        .andExpect(jsonPath("$.shoes[1].name", is("Shoe 2")));
  }

  @Test
  void search_invalidShoeFilter_shouldBeBadRequest() throws Exception {
    // given

    // when
    mockMvc.perform(get("/shoes/search")
            .header("version", 0)
            .param("size", "1")
            .param("color", "INVALID_COLOR"))
        // then
        .andExpect(status().isBadRequest());
  }
}
