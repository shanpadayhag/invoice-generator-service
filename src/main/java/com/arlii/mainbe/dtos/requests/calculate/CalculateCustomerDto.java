package com.arlii.mainbe.dtos.requests.calculate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CalculateCustomerDto {
  @NotBlank(message="Customer name is required.")
  private String name;

  @NotBlank(message="Customer address is required.")
  private String address;
}
