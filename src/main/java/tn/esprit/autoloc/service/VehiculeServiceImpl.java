package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.*;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;
import tn.esprit.autoloc.web.mapper.VehiculeMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;
    private final IAgenceRepository agenceRepository;
    private final IEquipementRepository equipementRepository;
    private final IMaintenanceRepository maintenanceRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public Vehicule create(Vehicule vehicule) {
        validate(vehicule);

        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant.");
        }

        resolveAssociations(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Véhicule", id));
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

    // 3. Affecter un équipement à un véhicule
    @Override
    public VehiculeResponseDto affecterVehiculeAEquipement(
            Long idVehicule, Long idEquipement) {

        Vehicule vehicule = findById(idVehicule);

        Equipement equipement = equipementRepository.findById(idEquipement)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Équipement",
                                idEquipement));

        vehicule.getEquipements().add(equipement);

        equipement.getVehicules().add(vehicule);

        Vehicule saved = vehiculeRepository.save(vehicule);

        return vehiculeMapper.toDto(saved);
    }

    // 4. Affecter une maintenance à un véhicule
    @Override
    public VehiculeResponseDto affecterMaintenanceAvehicule(
            String description, String immatriculation) {

        if (description == null || description.isBlank()
                || immatriculation == null || immatriculation.isBlank()) {
            throw new IllegalArgumentException(
                    "La description et l'immatriculation sont obligatoires.");
        }

        Vehicule vehicule = vehiculeRepository
                .findByImmatriculation(immatriculation)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Véhicule immatriculé", immatriculation));

        Maintenance maintenance = new Maintenance();

        maintenance.setDescription(description);
        maintenance.setDateDebut(LocalDate.now());
        maintenance.setDateFin(null);
        maintenance.setCout(BigDecimal.ZERO);
        maintenance.setVehicule(vehicule);

        maintenanceRepository.save(maintenance);

        vehicule.setStatut(StatutVehicule.MAINTENANCE);

        return vehiculeMapper.toDto(
                vehiculeRepository.save(vehicule));
    }

    private void validate(Vehicule vehicule) {

        if (vehicule == null) {
            throw new IllegalArgumentException(
                    "Le véhicule est obligatoire.");
        }

        BigDecimal tarif = vehicule.getTarifJournalier();

        if (tarif == null || tarif.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Tarif journalier invalide.");
        }
    }

    private void resolveAssociations(Vehicule vehicule) {

        if (vehicule.getAgence() == null
                || vehicule.getAgence().getIdAgence() == null) {
            throw new IllegalArgumentException(
                    "L'agence du véhicule est obligatoire.");
        }

        Long agenceId = vehicule.getAgence().getIdAgence();

        Agence agence = agenceRepository.findById(agenceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Agence", agenceId));

        vehicule.setAgence(agence);

        if (vehicule.getEquipements() == null
                || vehicule.getEquipements().isEmpty()) {
            vehicule.setEquipements(new java.util.HashSet<>());
            return;
        }

        java.util.Set<Equipement> equipements =
                new java.util.HashSet<>();

        for (Equipement equipement : vehicule.getEquipements()) {

            Long id = equipement.getIdEquipement();

            if (id == null) {
                throw new IllegalArgumentException(
                        "Identifiant équipement obligatoire.");
            }

            equipements.add(
                    equipementRepository.findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Équipement", id)));
        }

        vehicule.setEquipements(equipements);
    }
}
