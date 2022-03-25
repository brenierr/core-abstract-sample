package com.example.demo.core.service;

import com.example.demo.core.AbstractStockCore;
import com.example.demo.core.Implementation;
import com.example.demo.core.domain.ShoeId;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.core.exception.StockFullException;
import com.example.demo.core.mapper.DtoToEntityMapper;
import com.example.demo.core.mapper.EntityToDtoMapper;
import com.example.demo.core.repository.ShoeStockRepository;
import com.example.demo.dto.in.ShoeStockChange;
import com.example.demo.dto.out.ShoeStock;
import com.example.demo.dto.out.ShopStock;
import com.example.demo.dto.out.ShopStock.State;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Implementation(version = 3)
@Slf4j
public class StockShopCore extends AbstractStockCore {

  @Value("${shop.shoes.boxes.maximum_allowed:30}")
  private BigInteger maximumShoesBoxes;

  private final ShoeStockRepository shoeStockRepository;

  @Override
  public ShopStock getShopStock() {
    log.info("get shop stock");

    List<ShoeStock> stocksDto = shoeStockRepository.findAll()
        .stream()
        .map(EntityToDtoMapper::toShoeStockDto)
        .collect(Collectors.toList());

    return ShopStock.builder()
        .state(getStockState())
        .shoes(stocksDto)
        .build();
  }

  private State getStockState() {
    BigInteger totalQuantity = shoeStockRepository.getTotalQuantity().orElse(BigInteger.ZERO);
    if (BigInteger.ZERO.equals(totalQuantity)) {
      return State.EMPTY;
    }
    if (maximumShoesBoxes.equals(totalQuantity)) {
      return State.FULL;
    }
    return State.SOME;
  }

  @Override
  public void shoeStockChange(ShoeStockChange shoeStockChange) {
    log.info("shop stock change {}", shoeStockChange);

    validateMaximumStockNotReached(shoeStockChange);

    ShoeId shoeId = DtoToEntityMapper.map(shoeStockChange);
    shoeStockRepository.findById(shoeId)
        .ifPresentOrElse(
            shoeStock -> updateShoeStock(shoeStockChange, shoeStock),
            () -> createShoeInStock(shoeStockChange, shoeId)
        );
  }

  private void createShoeInStock(ShoeStockChange shoeStockChange, ShoeId shoeId) {
    log.info("create a shoe stock {} with quantity {}", shoeId, shoeStockChange.getQuantity());

    shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(shoeId.getColor())
            .name(shoeId.getName())
            .size(shoeId.getSize())
            .quantity(shoeStockChange.getQuantity())
            .build()
    );
  }

  private void updateShoeStock(ShoeStockChange shoeStockChange, ShoeStockEntity shoeStock) {
    log.info(
        "change shoe stock {} quantity: old={}, new={}",
        shoeStockChange.getShoe(),
        shoeStockChange.getQuantity()
    );

    shoeStock.setQuantity(shoeStockChange.getQuantity());
    shoeStockRepository.save(shoeStock);
  }

  private void validateMaximumStockNotReached(ShoeStockChange shoeStockChange) {
    Optional<BigInteger> currentBoxesQuantity = shoeStockRepository.getTotalQuantity();
    BigInteger newQuantity = currentBoxesQuantity
        .orElse(BigInteger.ZERO)
        .add(shoeStockChange.getQuantity());
    if (stockFull(newQuantity)) {
      String errorMessage = "Stock is full : try to store [" + newQuantity
          + "] shoe boxes when maximum allowed is [" + maximumShoesBoxes + "]";
      log.error(errorMessage);
      throw new StockFullException(errorMessage);
    }
  }

  private boolean stockFull(BigInteger newQuantity) {
    return newQuantity.compareTo(maximumShoesBoxes) == 1;
  }
}
