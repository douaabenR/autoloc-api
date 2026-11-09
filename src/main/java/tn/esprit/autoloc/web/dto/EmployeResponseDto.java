package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.RoleEmploye;

public record EmployeResponseDto(Long idEmploye, String nom, String prenom, RoleEmploye role, Long agenceId) {
}
