package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.web.dto.AgenceRequestDto;
import tn.esprit.autoloc.web.dto.AgenceResponseDto;

@Component
public class AgenceMapper {
    public Agence toEntity(AgenceRequestDto dto) {
        Agence agence = new Agence();
        agence.setNom(dto.nom());
        agence.setVille(dto.ville());
        agence.setAdresse(dto.adresse());
        agence.setTelephone(dto.telephone());
        return agence;
    }

    public AgenceResponseDto toDto(Agence agence) {
        return new AgenceResponseDto(agence.getIdAgence(), agence.getNom(), agence.getVille(),
                agence.getAdresse(), agence.getTelephone());
    }
}
