package com.mortgage.controller;

import com.mortgage.common.Result;
import com.mortgage.model.dto.CompareRequest;
import com.mortgage.model.dto.CompareResponse;
import com.mortgage.service.CompareService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compare")
@CrossOrigin
public class CompareController {
    private final CompareService compareService;

    public CompareController(CompareService compareService) {
        this.compareService = compareService;
    }

    @PostMapping
    public Result<CompareResponse> compare(@Valid @RequestBody CompareRequest request) {
        CompareResponse response = compareService.compare(request);
        return Result.success(response);
    }
}
