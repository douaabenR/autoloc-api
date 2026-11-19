package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.web.dto.ContratResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface IContratService {
    Contrat create(Contrat contrat);

    Contrat findById(Long id);

    List<Contrat> findAll();

    Contrat update(Long id, Contrat contrat);

    void deleteById(Long id);

    ContratResponseDto affecterContratAReservation(
            LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin);
}
