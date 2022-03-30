package com.example.demo.core.repository;

import com.example.demo.core.domain.Color;
import com.example.demo.core.domain.ShoeId;
import com.example.demo.core.domain.ShoeStockEntity;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeStockRepository extends JpaRepository<ShoeStockEntity, ShoeId> {

  @Query("SELECT s FROM ShoeStockEntity s"
      + " WHERE (:size is null or s.size = :size)"
      + " and (:color is null or s.color = :color)")
  List<ShoeStockEntity> findBySizeAndColor(
      @Param("size") BigInteger size,
      @Param("color") Color color
  );

  @Query("SELECT SUM(s.quantity) FROM ShoeStockEntity s")
  Optional<BigInteger> getTotalQuantity();
}
