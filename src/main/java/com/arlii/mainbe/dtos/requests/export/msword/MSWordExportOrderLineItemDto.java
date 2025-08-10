package com.arlii.mainbe.dtos.requests.export.msword;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MSWordExportOrderLineItemDto {
  @Min(value = 1, message = "Order quantity must be at least 1.")
  private Integer quantity;

  private String unit;

  @NotBlank(message = "Order description is required.")
  private String description;

  @NotNull(message = "Order unit price is required.")
  private BigDecimal unitPrice;
}
