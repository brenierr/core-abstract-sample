package com.example.demo.dto.in;

import com.example.demo.dto.in.ShoeFilter.Color;
import java.math.BigInteger;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ShoeKey {
  @NotEmpty
  String name;
  @NotNull @Min(0)
  BigInteger size;
  @NotNull
  Color color;
}
