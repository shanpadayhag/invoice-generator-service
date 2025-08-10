package com.arlii.mainbe.integration.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class MSWordExportControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldReturnOkWhenRequestContainsValidSingleOrder() throws Exception {
    // arrange
    Map<String, Object> customer = new HashMap<>();
    customer.put("name", "John Doe");
    customer.put("address", "123 Main St");
    Map<String, Object> order = new HashMap<>();
    order.put("quantity", null);
    order.put("unit", null);
    order.put("description", "Less advance payment");
    order.put("unitPrice", -7200);

    Map<String, Object> requestBodyMap = new HashMap<>();
    requestBodyMap.put("downpayment", 10050);
    requestBodyMap.put("customer", customer);
    requestBodyMap.put("orders", List.of(order));

    ObjectMapper objectMapper = new ObjectMapper();
    String requestBodyJson = objectMapper.writeValueAsString(requestBodyMap);

    // act

    // assert
    mockMvc.perform(MockMvcRequestBuilders.post("/export/msword")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBodyJson))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  void shouldReturnOkWhenOrderItemHasNullQuantity() throws Exception {
    // arrange

    // act

    // assert
  }

  @Test
  void shouldReturnOkWhenOrderItemHasNullQuantityAndUnit() throws Exception {
    // arrange

    // act

    // assert
  }

  @Test
  void itemWithOnlyNullUnit_returns400() throws Exception {
    // arrange

    // act

    // assert
  }

  @Test
  void multipleCompleteItems_returns200() throws Exception {
    // arrange

    // act

    // assert
  }

  @Test
  void mixedProcessableItems_returns200() throws Exception {
    // arrange

    // act

    // assert
  }

  @Test
  void listContainingInvalidItem_returns400() throws Exception {
    // arrange

    // act

    // assert
  }
}
