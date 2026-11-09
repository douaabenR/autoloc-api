package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgenceRequestDto(
        @NotBlank @Size(max = 100) String nom,
        @NotBlank @Size(max = 50) String ville,
        @NotBlank @Size(max = 150) String adresse,
        @Size(max = 20) String telephone) {
}
