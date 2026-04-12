package com.mortgage.controller;

import com.mortgage.common.Result;
import com.mortgage.model.dto.PrepayRequest;
import com.mortgage.model.dto.PrepayResponse;
import com.mortgage.service.PrepayService;
import com.mortgage.service.ValidatorService;
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
    private final ValidatorService validatorService;

    public PrepayController(PrepayService prepayService, ValidatorService validatorService) {
        this.prepayService = prepayService;
        this.validatorService = validatorService;
    }

    @PostMapping
    public Result<PrepayResponse> simulate(@Valid @RequestBody PrepayRequest request) {
        validatorService.validatePrepayRequest(request);
        PrepayResponse response = prepayService.simulatePrepay(request);
        return Result.success(response);
    }
}
