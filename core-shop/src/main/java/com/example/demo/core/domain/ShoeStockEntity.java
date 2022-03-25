package com.example.demo.core.domain;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "SHOE_STOCK")
@IdClass(ShoeId.class)
public class ShoeStockEntity {

  @Id
  @NotEmpty
  private String name;

  @Id
  @Min(0)
  @NotNull
  private BigInteger size;

  @Id
  @NotNull
  private Color color;

  @Column
  @Builder.Default
  @Min(0)
  @NotNull
  private BigInteger quantity = BigInteger.ZERO;

}
