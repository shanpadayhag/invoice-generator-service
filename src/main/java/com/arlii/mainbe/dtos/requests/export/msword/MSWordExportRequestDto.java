package com.arlii.mainbe.dtos.requests.export.msword;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MSWordExportRequestDto {
  private BigDecimal vat;
  private BigDecimal downpayment;

  @Valid
  @NotNull(message = "Customer information is required.")
  private MSWordExportCustomerDto customer;

  @Valid
  @NotNull(message = "Orders cannot be null.")
  @Size(min = 1, message = "At least one order is required.")
  private List<MSWordExportOrderLineItemDto> orders;
}
