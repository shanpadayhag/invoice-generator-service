package com.arlii.mainbe.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.arlii.mainbe.dtos.requests.calculate.CalculateRequestDto;

@RestController
public class CalculateController {

  @PostMapping("/calculate")
  CalculateRequestDto invoke(@RequestBody @Validated CalculateRequestDto request) {
    return request;
  }

}
