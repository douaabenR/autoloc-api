package tn.esprit.autoloc.web.dto;

import java.time.LocalDate;

public record ClientResponseDto(Long idClient, String nom, String prenom, String email, String telephone,
                                String numPermis, LocalDate dateInscription) {
}
