package com.example.day_2.service.impl;

import com.example.day_2.dto.reponse.BaseResponse;
import com.example.day_2.dto.reponse.CharAnalysisResponse;
import com.example.day_2.entity.CharAnalysis;
import com.example.day_2.repository.CharAnalysisRepository;
import com.example.day_2.service.CharAnalysisService;
import com.example.day_2.util.CharCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final CharAnalysisRepository charAnalysisRepository;

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null  || kata.isBlank()) {
            semua = charAnalysisRepository.findAll();
        } else {
            semua = charAnalysisRepository.findByKalimatContainingIgnoreCase(kata);
        }

        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data : semua) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    private CharAnalysisResponse toResponse(CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }

    @Override
    public BaseResponse<CharAnalysisResponse> getById(Long id) {

        Optional<CharAnalysis> result = charAnalysisRepository.findById(id);

        if (result.isEmpty()) {
            return BaseResponse.error(
                    "Data dengan id " + id + " tidak ditemukan"
            );
        }

        CharAnalysis entity = result.get();

        // Hitung ulang frekuensi agar response detail lengkap
        CharCounter counter = new CharCounter(entity.getKalimat());

        CharAnalysisResponse response = new CharAnalysisResponse(
                entity.getId(),
                entity.getKalimat(),
                entity.getTotalHuruf(),
                counter.getFrekuensi()
        );

        return BaseResponse.ok(response);
    }

    @Override
    public BaseResponse<List<CharAnalysis>> findMinimal(Integer min) {
        List<CharAnalysis> result = charAnalysisRepository.findMinimal(min);
        return BaseResponse.ok(result);
    }

    @Override
    public BaseResponse<List<CharAnalysis>> findLatest() {
        List<CharAnalysis> result = charAnalysisRepository.findLatest();
        return BaseResponse.ok(result);
    }

}
