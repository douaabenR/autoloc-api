package tn.esprit.autoloc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MaintenanceResponseDto(Long idMaintenance, LocalDate dateDebut, LocalDate dateFin,
                                     String description, BigDecimal cout, Long vehiculeId) {
}
