package tn.esprit.autoloc.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import tn.esprit.autoloc.domain.ModePaiement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaiementRequestDto(
        @NotNull @DecimalMin("0.0") @Digits(integer = 8, fraction = 2) BigDecimal montant,
        @NotNull LocalDateTime datePaiement,
        @NotNull ModePaiement modePaiement) {
}
