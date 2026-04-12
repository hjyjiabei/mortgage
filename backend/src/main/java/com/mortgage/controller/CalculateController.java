package com.mortgage.controller;

import com.mortgage.common.Result;
import com.mortgage.model.dto.CalculateRequest;
import com.mortgage.model.dto.CalculateResponse;
import com.mortgage.service.CalculateService;
import com.mortgage.service.ValidatorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/calculate")
@CrossOrigin
public class CalculateController {
    private final CalculateService calculateService;
    private final ValidatorService validatorService;

    public CalculateController(CalculateService calculateService, ValidatorService validatorService) {
        this.calculateService = calculateService;
        this.validatorService = validatorService;
    }

    @PostMapping
    public Result<CalculateResponse> calculate(@Valid @RequestBody CalculateRequest request) {
        validatorService.validateCalculateRequest(request);
        CalculateResponse response = calculateService.calculateAndSave(request);
        return Result.success(response);
    }

    @PostMapping("/preview")
    public Result<CalculateResponse> preview(@Valid @RequestBody CalculateRequest request) {
        validatorService.validateCalculateRequest(request);
        CalculateResponse response = calculateService.calculate(request);
        return Result.success(response);
    }
}
