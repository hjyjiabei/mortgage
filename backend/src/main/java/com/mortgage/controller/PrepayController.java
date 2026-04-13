package com.mortgage.controller;

import com.mortgage.common.Result;
import com.mortgage.model.dto.PrepayCompareResponse;
import com.mortgage.model.dto.PrepaySimulateRequest;
import com.mortgage.service.PrepayService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prepay")
@CrossOrigin
public class PrepayController {
    private final PrepayService prepayService;

    public PrepayController(PrepayService prepayService) {
        this.prepayService = prepayService;
    }

    @PostMapping("/simulate")
    public Result<PrepayCompareResponse> simulate(@Valid @RequestBody PrepaySimulateRequest request) {
        PrepayCompareResponse response = prepayService.simulatePrepay(request);
        return Result.success(response);
    }
}