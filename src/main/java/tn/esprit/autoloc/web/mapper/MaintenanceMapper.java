package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.MaintenanceRequestDto;
import tn.esprit.autoloc.web.dto.MaintenanceResponseDto;

@Component
public class MaintenanceMapper {
    public Maintenance toEntity(MaintenanceRequestDto dto) {
        Vehicule vehicule = null;
        if (dto.vehiculeId() != null) {
            vehicule = new Vehicule();
            vehicule.setIdVehicule(dto.vehiculeId());
        }
        return new Maintenance(null, dto.dateDebut(), dto.dateFin(), dto.description(), dto.cout(), vehicule);
    }

    public MaintenanceResponseDto toDto(Maintenance maintenance) {
        return new MaintenanceResponseDto(maintenance.getIdMaintenance(), maintenance.getDateDebut(),
                maintenance.getDateFin(), maintenance.getDescription(), maintenance.getCout(),
                maintenance.getVehicule() == null ? null : maintenance.getVehicule().getIdVehicule());
    }
}
