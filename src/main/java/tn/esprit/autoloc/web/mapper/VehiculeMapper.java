package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeRequestDto;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class VehiculeMapper {
    public Vehicule toEntity(VehiculeRequestDto dto) {
        Agence agence = new Agence();
        agence.setIdAgence(dto.agenceId());
        Set<Equipement> equipements = dto.equipementIds() == null ? Set.of() : dto.equipementIds().stream()
                .map(id -> {
                    Equipement equipement = new Equipement();
                    equipement.setIdEquipement(id);
                    return equipement;
                }).collect(Collectors.toSet());
        Vehicule vehicule = new Vehicule();
        vehicule.setImmatriculation(dto.immatriculation());
        vehicule.setMarque(dto.marque());
        vehicule.setModele(dto.modele());
        vehicule.setCategorie(dto.categorie());
        vehicule.setTarifJournalier(dto.tarifJournalier());
        vehicule.setStatut(dto.statut());
        vehicule.setAgence(agence);
        vehicule.setEquipements(equipements);
        return vehicule;
    }

    public VehiculeResponseDto toDto(Vehicule vehicule) {
        Set<Long> equipementIds = vehicule.getEquipements() == null ? Set.of() : vehicule.getEquipements().stream()
                .map(Equipement::getIdEquipement).collect(Collectors.toSet());
        return new VehiculeResponseDto(vehicule.getIdVehicule(), vehicule.getImmatriculation(), vehicule.getMarque(),
                vehicule.getModele(), vehicule.getCategorie(), vehicule.getTarifJournalier(), vehicule.getStatut(),
                vehicule.getAgence() == null ? null : vehicule.getAgence().getIdAgence(), equipementIds);
    }
}
