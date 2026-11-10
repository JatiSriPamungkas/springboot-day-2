package com.bootcamp19.helloworld.service.impl;

import com.bootcamp19.helloworld.dto.feign.QuoteResponse;
import com.bootcamp19.helloworld.dto.request.CharAnalysisRequest;
import com.bootcamp19.helloworld.dto.response.CharAnalysisResponse;
import com.bootcamp19.helloworld.entity.CharAnalysis;
import com.bootcamp19.helloworld.exception.AnalysisConflictException;
import com.bootcamp19.helloworld.exception.AnalysisNotFoundException;
import com.bootcamp19.helloworld.exception.QuoteUnavailableException;
import com.bootcamp19.helloworld.repository.CharAnalysisRepository;
import com.bootcamp19.helloworld.rest.QuoteClient;
import com.bootcamp19.helloworld.service.CharAnalysisService;
import com.bootcamp19.helloworld.util.CharCounter;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharAnalysisServiceImpl implements CharAnalysisService {

    private final QuoteClient client;
    private final CharAnalysisRepository charAnalysisRepository;

    @Override
    public List<CharAnalysisResponse> cari(String kata) {
        List<CharAnalysis> semua;
        if (kata == null || kata.isBlank()) {
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

    @Override
    public CharAnalysisResponse simpan(CharAnalysisRequest request) {
        String kalimat = request != null ? request.getKalimat() : "";
        if (charAnalysisRepository.existsByKalimatIgnoreCase(kalimat)) {
            throw new AnalysisConflictException(kalimat);
        }

        return simpan(kalimat, null);
    }

    private CharAnalysisResponse simpan(String kalimat, String author) {
        CharCounter counter = new CharCounter(kalimat);
        CharAnalysis data = new CharAnalysis();

        data.setKalimat(kalimat);
        data.setAuthor(author);
        data.setTotalHuruf(counter.getTotalHuruf());
        data.setCreatedAt(LocalDateTime.now());

        charAnalysisRepository.save(data);
        return CharAnalysisResponse.from(data, counter.toMap());
    }

    @Override
    public Optional<CharAnalysisResponse> getById(Long id) {
        return charAnalysisRepository.findById(id)
                .map(this::toResponse);
    }

    @Override
    public List<CharAnalysisResponse> getMinimal(int min) {
        List<CharAnalysis> list = charAnalysisRepository.findByMinimalHuruf(min);
        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data : list) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public List<CharAnalysisResponse> getTerbaru() {
        List<CharAnalysis> list = charAnalysisRepository.findTop5Terbaru();
        List<CharAnalysisResponse> hasil = new ArrayList<>();
        for (CharAnalysis data : list) {
            hasil.add(toResponse(data));
        }
        return hasil;
    }

    @Override
    public CharAnalysisResponse analisisDariQuote() {
        QuoteResponse quote;
        try {
            quote = client.getQuote();
        } catch (FeignException feignException) {
            throw new QuoteUnavailableException("Service Unavailable!");
        }

        return simpan(quote.getQuote(), quote.getAuthor());
    }

    @Override
    public CharAnalysisResponse analisisDariQuoteById(Long id) {
        QuoteResponse quote;
        try {
            quote = client.getQuoteById(id);
        } catch (FeignException.NotFound notFoundException) {
            throw new AnalysisNotFoundException("Quote dengan id " + id + " tidak ditemukan di DummyJSON");
        } catch (FeignException feignException) {
            throw new QuoteUnavailableException("Service Unavailable!");
        }

        if (charAnalysisRepository.existsByKalimatIgnoreCase(quote.getQuote())) {
            throw new AnalysisConflictException(quote.getQuote());
        }

        return simpan(quote.getQuote(), quote.getAuthor());
    }

    private CharAnalysisResponse toResponse(CharAnalysis data) {
        CharCounter counter = new CharCounter(data.getKalimat());
        return CharAnalysisResponse.from(data, counter.toMap());
    }
}
