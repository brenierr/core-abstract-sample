package com.example.demo.core;

import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeStockEntity;
import com.example.demo.core.repository.ShoeStockRepository;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
  @Autowired
  protected ShoeStockRepository shoeStockRepository;

  protected ShoeStockEntity createStockEntity(BigInteger size, String name, Color color,
      BigInteger quantity) {
    return shoeStockRepository.save(
        ShoeStockEntity.builder()
            .color(color)
            .name(name)
            .size(size)
            .quantity(quantity)
            .build()
    );
  }

}
