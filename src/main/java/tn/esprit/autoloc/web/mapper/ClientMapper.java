package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.web.dto.ClientRequestDto;
import tn.esprit.autoloc.web.dto.ClientResponseDto;

@Component
public class ClientMapper {
    public Client toEntity(ClientRequestDto dto) {
        Client client = new Client();
        client.setNom(dto.nom());
        client.setPrenom(dto.prenom());
        client.setEmail(dto.email());
        client.setTelephone(dto.telephone());
        client.setNumPermis(dto.numPermis());
        client.setDateInscription(dto.dateInscription());
        return client;
    }

    public ClientResponseDto toDto(Client client) {
        return new ClientResponseDto(client.getIdClient(), client.getNom(), client.getPrenom(), client.getEmail(),
                client.getTelephone(), client.getNumPermis(), client.getDateInscription());
    }
}
