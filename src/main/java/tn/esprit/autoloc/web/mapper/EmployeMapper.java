package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.web.dto.EmployeRequestDto;
import tn.esprit.autoloc.web.dto.EmployeResponseDto;

@Component
public class EmployeMapper {
    public Employe toEntity(EmployeRequestDto dto) {
        Agence agence = new Agence();
        agence.setIdAgence(dto.agenceId());
        return new Employe(null, dto.nom(), dto.prenom(), dto.role(), agence);
    }

    public EmployeResponseDto toDto(Employe employe) {
        return new EmployeResponseDto(employe.getIdEmploye(), employe.getNom(), employe.getPrenom(),
                employe.getRole(), employe.getAgence() == null ? null : employe.getAgence().getIdAgence());
    }
}
