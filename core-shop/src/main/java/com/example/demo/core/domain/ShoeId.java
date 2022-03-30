package com.example.demo.core.domain;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class ShoeId implements Serializable {
  @Column(nullable = false)
  @NotEmpty
  private String name;

  @Column(nullable = false)
  @Min(0)
  private BigInteger size;

  @Column(nullable = false)
  private Color color;
}
