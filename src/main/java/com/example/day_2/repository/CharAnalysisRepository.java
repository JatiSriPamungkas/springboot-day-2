package com.example.day_2.repository;

import com.example.day_2.entity.CharAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CharAnalysisRepository extends JpaRepository<CharAnalysis, Long> {
    List<CharAnalysis> findByKalimatContainingIgnoreCase(String kata);
    // JPQL
    @Query("SELECT c FROM CharAnalysis c WHERE c.totalHuruf >= :min")
    List<CharAnalysis> findMinimal(@Param("min") Integer min);

    // Native Query
    @Query(
            value = "SELECT * FROM mst_char_analysis " +
                    "ORDER BY created_at DESC LIMIT 5",
            nativeQuery = true
    )
    List<CharAnalysis> findLatest();
}
