package com.arlii.mainbe.dtos.requests.calculate;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CalculateRequestDto {
  private BigDecimal vat;
  private BigDecimal downpayment;

  @Valid
  @NotNull(message = "Customer information is required.")
  private CalculateCustomerDto customer;

  @Valid
  @NotNull(message = "Order line items cannot be null.")
  @Size(min = 1, message = "At least one order line item is required.")
  private List<CalculateOrderLineItemDto> orderLineItem;
}
