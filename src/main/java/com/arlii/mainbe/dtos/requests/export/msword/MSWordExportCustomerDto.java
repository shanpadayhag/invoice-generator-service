package com.arlii.mainbe.dtos.requests.export.msword;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MSWordExportCustomerDto {
  @NotBlank(message="Customer name is required.")
  private String name;

  @NotBlank(message="Customer address is required.")
  private String address;
}
