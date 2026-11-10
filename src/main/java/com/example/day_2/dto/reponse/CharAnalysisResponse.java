package com.example.day_2.dto.reponse;

import com.example.day_2.entity.CharAnalysis;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class CharAnalysisResponse {
    private Long id;
    private String kata;
    private int totalHuruf;
    private Map<Character, Integer> frekuensi;

    public static CharAnalysisResponse from(CharAnalysis data, Map<Character, Integer> frekuensi) {
        return new CharAnalysisResponse(data.getId(), data.getKalimat(), data.getTotalHuruf(), frekuensi);
    }
}
