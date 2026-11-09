package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record EquipementRequestDto(
        @NotBlank @Size(max = 100) String libelle,
        @NotNull @PositiveOrZero Integer quantite) {
}
