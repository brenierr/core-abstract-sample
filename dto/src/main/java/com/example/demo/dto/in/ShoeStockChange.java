package com.example.demo.dto.in;

import java.math.BigInteger;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoeStockChange {

  @Valid @NotNull
  ShoeKey shoe;

  @NotNull @Min(0) @Max(30)
  BigInteger quantity;

}
