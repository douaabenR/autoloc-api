package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContratResponseDto(Long idContrat, LocalDate dateSignature, BigDecimal montantTotal,
                                 boolean valide, Long reservationId, List<Long> paiementIds) {
}
