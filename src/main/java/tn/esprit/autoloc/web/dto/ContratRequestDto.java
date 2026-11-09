package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContratRequestDto(
        @NotNull LocalDate dateSignature,
        @NotNull @DecimalMin("0.0") @Digits(integer = 8, fraction = 2) BigDecimal montantTotal,
        @NotNull Boolean valide,
        @NotNull @Positive Long reservationId,
        @Valid List<PaiementRequestDto> paiements) {
}
