package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.Set;

public record VehiculeRequestDto(
        @NotBlank @Size(max = 20) String immatriculation,
        @NotBlank @Size(max = 50) String marque,
        @NotBlank @Size(max = 50) String modele,
        @NotNull CategorieVehicule categorie,
        @NotNull @DecimalMin("0.0") @Digits(integer = 8, fraction = 2) BigDecimal tarifJournalier,
        @NotNull StatutVehicule statut,
        @NotNull @Positive Long agenceId,
        Set<@NotNull @Positive Long> equipementIds) {
}
