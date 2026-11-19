package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;

import java.util.List;

public interface IReservationService {

    Reservation create(Reservation reservation);

    Reservation findById(Long id);

    List<Reservation> findAll();

    Reservation update(Long id, Reservation reservation);

    void deleteById(Long id);

    ReservationResponseDto affecterReservationAClient(
            Long idReservation, String nom, String prenom);

    ReservationResponseDto creerReservationEtContratAssocie(
            ReservationWithContratRequestDTO requestDTO);
}