package com.arlii.mainbe.unit.dtos.requests.export.msword;

import java.math.BigDecimal;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportOrderLineItemDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class MSWordExportOrderLineItemDtoTest {

  private Validator validator;

  @BeforeEach
  void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldSetAndGetFieldsCorrectlyWhenValidValuesAreProvided() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();

    // act
    dto.setQuantity(15);
    dto.setUnit("kilo");
    dto.setDescription("Live eels, 500 grams per piece");
    dto.setUnitPrice(new BigDecimal(1200));

    // assert
    Assertions.assertThat(dto.getQuantity()).isEqualTo(15);
    Assertions.assertThat(dto.getUnit()).isEqualTo("kilo");
    Assertions.assertThat(dto.getDescription()).isEqualTo("Live eels, 500 grams per piece");
    Assertions.assertThat(dto.getUnitPrice()).isEqualTo(new BigDecimal(1200));
  }

  @Test
  void quantity_ShouldPassValidationWhenIsOne() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();
    dto.setQuantity(1);
    dto.setUnit("kilo");
    dto.setDescription("Live eels, 500 grams per piece");
    dto.setUnitPrice(new BigDecimal(1200));

    // act
    Set<ConstraintViolation<MSWordExportOrderLineItemDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(0);
  }

  @Test
  void quantity_ShouldPassValidationWhenIsGreaterThanOne() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();
    dto.setQuantity(15);
    dto.setUnit("kilo");
    dto.setDescription("Live eels, 500 grams per piece");
    dto.setUnitPrice(new BigDecimal(1200));

    // act
    Set<ConstraintViolation<MSWordExportOrderLineItemDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(0);
  }

  @Test
  void quantity_ShouldFailValidationWhenIsZero() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();
    dto.setQuantity(0);
    dto.setUnit("kilo");
    dto.setDescription("Live eels, 500 grams per piece");
    dto.setUnitPrice(new BigDecimal(1200));

    // act
    Set<ConstraintViolation<MSWordExportOrderLineItemDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(1);
    ConstraintViolation<MSWordExportOrderLineItemDto> violation = violations.iterator().next();
    Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("quantity");
    Assertions.assertThat(violation.getMessage()).isEqualTo("Order quantity must be at least 1.");
  }

  @Test
  void quantity_ShouldFailValidationWhenIsNegative() {
    // arrange

    // act

    // assert
  }

  @Test
  void quantity_ShouldPassValidationWhenIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void description_ShouldPassValidationWhenIsNotBlank() {
    // arrange

    // act

    // assert
  }

  @Test
  void description_ShouldFailValidationWhenIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void description_ShouldFailValidationWhenIsEmpty() {
    // arrange

    // act

    // assert
  }

  @Test
  void description_ShouldFailValidationWhenIsWhitespaceOnly() {
    // arrange

    // act

    // assert
  }

  @Test
  void unitPrice_ShouldPassValidationWhenIsNotNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void unitPrice_ShouldFailValidationWhenIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void unit_ShouldAllowAnyStringValueIncludingNullAndEmpty() {
    // arrange

    // act

    // assert
  }

  @Test
  void dto_ShouldPassValidationWhenAllFieldsAreValid() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();
    dto.setQuantity(15);
    dto.setUnit("kilo");
    dto.setDescription("Live eels, 500 grams per piece");
    dto.setUnitPrice(new BigDecimal(1200));

    // act
    Set<ConstraintViolation<MSWordExportOrderLineItemDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(0);
  }

  @Test
  void dto_ShouldFailValidationAndReportMultipleViolationsWhenMultipleFieldsAreInvalid() {
    // arrange
    MSWordExportOrderLineItemDto dto = new MSWordExportOrderLineItemDto();
    dto.setQuantity(0);
    dto.setUnit("kilo");
    dto.setDescription("");
    dto.setUnitPrice(null);

    // act
    Set<ConstraintViolation<MSWordExportOrderLineItemDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(3);

    Assertions.assertThat(violations)
        .extracting(ConstraintViolation::getPropertyPath)
        .map(Object::toString)
        .containsExactlyInAnyOrder("quantity", "description", "unitPrice");

    Assertions.assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .containsExactlyInAnyOrder(
            "Order quantity must be at least 1.",
            "Order description is required.",
            "Order unit price is required.");
  }

}
