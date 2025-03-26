package com.zxcjabka.testovoe.api.controller;

import com.zxcjabka.testovoe.api.service.ReportService;
import com.zxcjabka.testovoe.api.service.model.CalorieCheckResponse;
import com.zxcjabka.testovoe.api.service.model.DailyNutritionResponse;
import com.zxcjabka.testovoe.api.service.model.DailyReportResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/daily-report/{userId}/{date}")
    public ResponseEntity<DailyReportResponse> getDailyReport(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        DailyReportResponse report = reportService.generateDailyReport(userId, date);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/calorie-check/{userId}/{date}")
    public ResponseEntity<CalorieCheckResponse> checkDailyCalories(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        CalorieCheckResponse response = reportService.checkCalorieIntake(userId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/nutrition-history/{userId}")
    public ResponseEntity<List<DailyNutritionResponse>> getNutritionHistory(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<DailyNutritionResponse> history = reportService.getNutritionHistory(userId, startDate, endDate);
        return ResponseEntity.ok(history);
    }
}
