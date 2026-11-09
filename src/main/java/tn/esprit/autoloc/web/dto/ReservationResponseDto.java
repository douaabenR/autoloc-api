package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;

public record ReservationResponseDto(Long idReservation, LocalDate dateDebut, LocalDate dateFin,
                                     StatutReservation statut, Long vehiculeId, Long clientId) {
}
