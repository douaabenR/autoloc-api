package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.web.dto.EquipementRequestDto;
import tn.esprit.autoloc.web.dto.EquipementResponseDto;

@Component
public class EquipementMapper {
    public Equipement toEntity(EquipementRequestDto dto) {
        Equipement equipement = new Equipement();
        equipement.setLibelle(dto.libelle());
        equipement.setQuantite(dto.quantite());
        return equipement;
    }

    public EquipementResponseDto toDto(Equipement equipement) {
        return new EquipementResponseDto(equipement.getIdEquipement(), equipement.getLibelle(), equipement.getQuantite());
    }
}
