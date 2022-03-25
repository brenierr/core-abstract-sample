package com.example.demo.core.service;

import com.example.demo.core.AbstractShoeCore;
import com.example.demo.core.Implementation;
import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.core.mapper.EntityToDtoMapper;
import com.example.demo.core.repository.ShoeStockRepository;
import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.Shoes;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Implementation(version = 3)
@Service
@RequiredArgsConstructor
@Slf4j
public class ShoeShopCore extends AbstractShoeCore {

  private final ShoeStockRepository shoeStockRepository;

  @Override
  public Shoes search(final ShoeFilter filter) {
    log.info("search shoes with color={}, size={}", filter.getColor(), filter.getSize());

    List<ShoeStockEntity> shoes = shoeStockRepository.findBySizeAndColor(
        filter.getSize().orElse(null),
        filter.getColor()
            .map(color -> Color.valueOf(color.name()))
            .orElse(null)
    );
    return Shoes.builder()
        .shoes(shoes.stream()
            .map(EntityToDtoMapper::toShoeDto)
            .collect(Collectors.toList()))
        .build();
  }
}
