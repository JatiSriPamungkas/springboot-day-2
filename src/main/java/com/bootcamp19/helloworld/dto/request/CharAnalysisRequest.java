package com.bootcamp19.helloworld.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharAnalysisRequest {

    @NotBlank(message = "Kalimat wajib isi")
    @Size(max = 500, message = "Kalimat maksimal 500 karakter")
    private String kalimat;
}
