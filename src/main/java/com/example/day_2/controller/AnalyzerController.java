package com.example.day_2.controller;

import com.example.day_2.dto.reponse.BaseResponse;
import com.example.day_2.dto.reponse.CharAnalysisResponse;
import com.example.day_2.service.CharAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnalyzerController {
    private final CharAnalysisService charAnalysisService;

    @GetMapping("/find")
    public BaseResponse<List<CharAnalysisResponse>> cari(@RequestParam(required = false) String kalimat){
        return BaseResponse.ok(charAnalysisService.cari(kalimat));
    }

    // GET /api/v1/minimal?min=10
    @GetMapping("/minimal")
    public BaseResponse<List<CharAnalysis>> findMinimal(
            @RequestParam Integer min
    ) {
        return service.findMinimal(min);
    }

    // GET /api/v1/terbaru
    @GetMapping("/terbaru")
    public BaseResponse<List<CharAnalysis>> findLatest() {
        return service.findLatest();
    }

    @PostMapping
    public BaseResponse<CharAnalysisResponse> analyze(
            @RequestBody CharAnalysisRequest request,
            @RequestHeader(
                    value = "X-Request-Id",
                    required = false
            ) String requestId
    ) {

        System.out.println(
                "X-Request-Id: " + requestId +
                        ", kalimat: " + request.getKalimat()
        );

        return service.analyzeAndSave(request, requestId);
    }
}
