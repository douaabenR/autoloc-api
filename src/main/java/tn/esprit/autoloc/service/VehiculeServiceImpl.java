package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;
import tn.esprit.autoloc.repository.IAgenceRepository;
import tn.esprit.autoloc.repository.IEquipementRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;

    @Override
    public Vehicule create(Vehicule vehicule) {
        validate(vehicule);
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException("Un nouveau véhicule ne doit pas avoir d'identifiant.");
        }
        resolveAssociations(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", id));
    }

    @Override
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    public Vehicule update(Long id, Vehicule vehicule) {
        validate(vehicule);
        Vehicule existing = findById(id);
        existing.setImmatriculation(vehicule.getImmatriculation());
        existing.setMarque(vehicule.getMarque());
        existing.setModele(vehicule.getModele());
        existing.setCategorie(vehicule.getCategorie());
        existing.setTarifJournalier(vehicule.getTarifJournalier());
        existing.setStatut(vehicule.getStatut());
        resolveAssociations(vehicule);
        existing.setAgence(vehicule.getAgence());
        existing.setEquipements(vehicule.getEquipements());
        return vehiculeRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Véhicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    private void validate(Vehicule vehicule) {
        if (vehicule == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire.");
        }
        BigDecimal tarif = vehicule.getTarifJournalier();
        if (tarif == null || tarif.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le tarif journalier du véhicule doit être positif ou nul.");
        }
    }

    private void resolveAssociations(Vehicule vehicule) {
        if (vehicule.getAgence() == null || vehicule.getAgence().getIdAgence() == null) {
            throw new IllegalArgumentException("L'agence du véhicule est obligatoire. Fournissez agenceId.");
        }
        Long agenceId = vehicule.getAgence().getIdAgence();
        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Agence", agenceId));
        vehicule.setAgence(agence);

        if (vehicule.getEquipements() == null || vehicule.getEquipements().isEmpty()) {
            vehicule.setEquipements(new java.util.HashSet<>());
            return;
        }
        java.util.Set<Equipement> equipements = new java.util.HashSet<>();
        for (Equipement equipement : vehicule.getEquipements()) {
            Long id = equipement.getIdEquipement();
            if (id == null) throw new IllegalArgumentException("Chaque équipement doit fournir son identifiant.");
            equipements.add(equipementRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Équipement", id)));
        }
        vehicule.setEquipements(equipements);
    }
}
