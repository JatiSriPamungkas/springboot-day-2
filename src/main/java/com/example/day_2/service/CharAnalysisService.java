package com.example.day_2.service;

import com.example.day_2.dto.reponse.BaseResponse;
import com.example.day_2.dto.reponse.CharAnalysisResponse;
import com.example.day_2.dto.request.CharAnalysisRequest;
import com.example.day_2.entity.CharAnalysis;

import java.util.List;

public interface CharAnalysisService {
    List<CharAnalysisResponse> cari(String kata);
    // Endpoint POST
    BaseResponse<CharAnalysisResponse> analyzeAndSave(
            CharAnalysisRequest request,
            String requestId
    );

    // Endpoint GET /{id}
    BaseResponse<CharAnalysisResponse> getById(Long id);

    // Endpoint GET /minimal
    BaseResponse<List<CharAnalysis>> findMinimal(Integer min);

    // Endpoint GET /terbaru
    BaseResponse<List<CharAnalysis>> findLatest();
}
