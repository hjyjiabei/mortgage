package com.mortgage.controller;

import com.mortgage.common.Result;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PlanDTO;
import com.mortgage.service.PlanService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/plan")
@CrossOrigin
public class PlanController {
    private final PlanService planService;

    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/{planId}")
    public Result<PlanDTO> getPlan(@PathVariable Long planId) {
        PlanDTO plan = planService.getPlanById(planId);
        return Result.success(plan);
    }

    @GetMapping("/{planId}/details")
    public Result<List<DetailDTO>> getDetails(@PathVariable Long planId) {
        List<DetailDTO> details = planService.getDetailsByPlanId(planId);
        return Result.success(details);
    }

    @GetMapping("/history")
    public Result<List<PlanDTO>> getHistory(@RequestParam(defaultValue = "1") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        List<PlanDTO> plans = planService.getHistory(page, size);
        return Result.success(plans);
    }
}
