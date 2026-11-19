package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.VehiculeResponseDto;

import java.util.List;

public interface IVehiculeService {
    Vehicule create(Vehicule vehicule);

    Vehicule findById(Long id);

    List<Vehicule> findAll();

    Vehicule update(Long id, Vehicule vehicule);

    void deleteById(Long id);

    VehiculeResponseDto affecterVehiculeAEquipement(Long idVehicule, Long idEquipement);

    VehiculeResponseDto affecterMaintenanceAvehicule(String description, String immatriculation);
}
