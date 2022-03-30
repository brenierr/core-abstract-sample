package com.example.demo.controller;

import com.example.demo.core.StockCore;
import com.example.demo.dto.in.ShoeStockChange;
import com.example.demo.dto.out.ShopStock;
import com.example.demo.facade.StockFacade;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/stock")
@RequiredArgsConstructor
@Slf4j
public class StockController {

  private final StockFacade stockFacade;

  @GetMapping
  public ResponseEntity<ShopStock> get(@RequestHeader(defaultValue = "3") Integer version) {
    log.info("call get shop stock, version {}", version);

    return ResponseEntity.ok(stockCore(version).getShopStock());
  }

  @PatchMapping
  public ResponseEntity<String> patchShoeStock(
      @Valid @RequestBody ShoeStockChange shoeStockChange,
      @RequestHeader(defaultValue = "3") Integer version) {
    log.info("call patch shop stock, version {}, change={}", version, shoeStockChange);

    stockCore(version).shoeStockChange(shoeStockChange);

    return ResponseEntity.ok(
        "Shoe [" + shoeStockChange + "] updated successfully."
    );
  }

  private StockCore stockCore(Integer version) {
    return stockFacade.get(version);
  }

}
