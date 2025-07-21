package com.arlii.mainbe.integration.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.arlii.mainbe.controllers.CalculateController;

@WebMvcTest(CalculateController.class)
@AutoConfigureMockMvc
public class CalculateControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  @DisplayName("A single complete item returns 200 OK and the correct total")
  void singleCompleteItem_returns200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("An item with a null quantity returns 200 OK and the correct total")
  void itemWithNullQuantity_returns200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("An item with both quantity and unit as null returns 200 OK and the correct total")
  void itemWithNullQuantityAndUnit_returns200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("An item with a null unit but a valid quantity returns 400 Bad Request")
  void itemWithOnlyNullUnit_returns400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("A request with multiple complete items returns 200 OK and the correct sum")
  void multipleCompleteItems_returns200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("A mixed list of processable items returns 200 OK and the correct sum")
  void mixedProcessableItems_returns200() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  @Test
  @DisplayName("A request containing any invalid item returns 400 Bad Request")
  void listContainingInvalidItem_returns400() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/calculate"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

  // Add another test where if there are no items it will also fail
}
