package tn.esprit.autoloc.web.dto;

import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;

import java.math.BigDecimal;
import java.util.Set;

public record VehiculeResponseDto(Long idVehicule, String immatriculation, String marque, String modele,
                                  CategorieVehicule categorie, BigDecimal tarifJournalier, StatutVehicule statut,
                                  Long agenceId, Set<Long> equipementIds) {
}
