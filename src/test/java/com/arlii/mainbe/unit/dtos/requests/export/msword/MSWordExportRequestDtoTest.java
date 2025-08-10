package com.arlii.mainbe.unit.dtos.requests.export.msword;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportCustomerDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportOrderLineItemDto;
import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportRequestDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MSWordExportRequestDtoTest {

  private Validator validator;

  @BeforeEach
  void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldSetAndGetFieldsCorrectlyWhenValidValuesAreProvided() {
    // arrange
    MSWordExportRequestDto dto = new MSWordExportRequestDto();
    MSWordExportCustomerDto customer = new MSWordExportCustomerDto();
    List<MSWordExportOrderLineItemDto> orders = new ArrayList<>();

    // act
    dto.setCustomer(customer);
    dto.setOrders(orders);

    // assert
    Assertions.assertThat(dto.getCustomer()).isEqualTo(customer);
    Assertions.assertThat(dto.getOrders()).isEqualTo(orders);
  }

  @Test
  void shouldPassValidationWhenAllFieldsAreValid() {
    // arrange
    MSWordExportRequestDto dto = new MSWordExportRequestDto();
    MSWordExportCustomerDto customer = new MSWordExportCustomerDto();
    customer.setName("John Doe");
    customer.setAddress("123 Main St");

    List<MSWordExportOrderLineItemDto> orders = new ArrayList<>();
    MSWordExportOrderLineItemDto order = new MSWordExportOrderLineItemDto();
    order.setQuantity(15);
    order.setUnit("kilo");
    order.setDescription("Live eels, 500 grams per piece");
    order.setUnitPrice(new BigDecimal(1200));
    orders.add(order);

    dto.setCustomer(customer);
    dto.setOrders(orders);

    // act
    Set<ConstraintViolation<MSWordExportRequestDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(0);
  }

  @Test
  void shouldFailValidationWhenCustomerIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldFailValidationWhenOrderLineItemIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldFailValidationWhenOrderLineItemIsLessThanOne() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldHaveCorrectEqualsAndHashCodeImplementation() {
    // arrange

    // act

    // assert
  }
}
