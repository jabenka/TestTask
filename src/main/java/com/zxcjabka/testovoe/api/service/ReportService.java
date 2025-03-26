package com.zxcjabka.testovoe.api.service;

import com.zxcjabka.testovoe.api.service.model.CalorieCheckResponse;
import com.zxcjabka.testovoe.api.service.model.DailyNutritionResponse;
import com.zxcjabka.testovoe.api.service.model.DailyReportResponse;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    DailyReportResponse generateDailyReport(Long userId, LocalDate date);

    CalorieCheckResponse checkCalorieIntake(Long userId, LocalDate date);

    List<DailyNutritionResponse> getNutritionHistory(Long userId, LocalDate startDate, LocalDate endDate);
}
