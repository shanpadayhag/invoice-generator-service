package com.arlii.mainbe.unit.dtos.requests.export.msword;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.arlii.mainbe.dtos.requests.export.msword.MSWordExportCustomerDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class MSWordExportCustomerDtoTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void shouldSetAndGetFieldsCorrectlyWhenValidValuesAreProvided() {
    // arrange
    MSWordExportCustomerDto dto = new MSWordExportCustomerDto();

    // act
    dto.setName("John Doe");
    dto.setAddress("123 Main St");

    // assert
    Assertions.assertThat(dto.getName()).isEqualTo("John Doe");
    Assertions.assertThat(dto.getAddress()).isEqualTo("123 Main St");
    Assertions.assertThat(dto.toString()).contains("John Doe", "123 Main St");
  }

  @Test
  void shouldPassValidationWhenAllFieldsAreValid() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldFailValidationWhenNameIsBlank() {
    // arrange
    MSWordExportCustomerDto dto = new MSWordExportCustomerDto();
    dto.setName("");
    dto.setAddress("123 Main St");

    // act
    Set<ConstraintViolation<MSWordExportCustomerDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(1);
    ConstraintViolation<MSWordExportCustomerDto> violation = violations.iterator().next();
    Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
    Assertions.assertThat(violation.getMessage()).isEqualTo("Customer name is required.");
  }

  @Test
  void shouldFailValidationWhenNameIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldFailValidationWhenAddressIsBlank() {
    // arrange
    MSWordExportCustomerDto dto = new MSWordExportCustomerDto();
    dto.setName("John Doe");
    dto.setAddress("");

    // act
    Set<ConstraintViolation<MSWordExportCustomerDto>> violations = validator.validate(dto);

    // assert
    Assertions.assertThat(violations).hasSize(1);
    ConstraintViolation<MSWordExportCustomerDto> violation = violations.iterator().next();
    Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("address");
    Assertions.assertThat(violation.getMessage()).isEqualTo("Customer address is required.");
  }

  @Test
  void shouldFailValidationWhenAddressIsNull() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldFailValidationWithTwoViolationsWhenBothNameAndAddressAreBlank() {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldHaveCorrectEqualsAndHashCodeImplementation() {
    // arrange
    MSWordExportCustomerDto dto1 = new MSWordExportCustomerDto();
    dto1.setName("Alice");
    dto1.setAddress("10 Wonderland");

    MSWordExportCustomerDto dto2 = new MSWordExportCustomerDto();
    dto2.setName("Alice");
    dto2.setAddress("10 Wonderland");

    MSWordExportCustomerDto dto3 = new MSWordExportCustomerDto();
    dto3.setName("Bob");
    dto3.setAddress("20 Oz");

    // assert
    Assertions.assertThat(dto1).isEqualTo(dto2);
    Assertions.assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    Assertions.assertThat(dto1).isNotEqualTo(dto3);
    Assertions.assertThat(dto1.hashCode()).isNotEqualTo(dto3.hashCode());
    Assertions.assertThat(dto1).isNotEqualTo(null);
    Assertions.assertThat(dto1).isNotEqualTo(new Object());
  }
}
