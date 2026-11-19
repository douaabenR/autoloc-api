package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.autoloc.domain.*;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.*;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;
import tn.esprit.autoloc.web.dto.ReservationWithContratRequestDTO;
import tn.esprit.autoloc.web.mapper.ReservationMapper;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;
    private final IContratRepository contratRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public Reservation create(Reservation reservation) {
        validate(reservation);

        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException(
                    "Une nouvelle réservation ne doit pas avoir d'identifiant.");
        }

        resolveAssociations(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Réservation", id));
    }

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation update(Long id, Reservation reservation) {
        validate(reservation);

        Reservation existing = findById(id);

        existing.setDateDebut(reservation.getDateDebut());
        existing.setDateFin(reservation.getDateFin());
        existing.setStatut(reservation.getStatut());
        existing.setVehicule(findVehicule(reservation));
        existing.setClient(findClient(reservation));

        return reservationRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Réservation", id);
        }

        reservationRepository.deleteById(id);
    }

    // 1. Affecter une réservation à un client
    @Override
    public ReservationResponseDto affecterReservationAClient(
            Long idReservation, String nom, String prenom) {

        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom et le prénom du client sont obligatoires.");
        }

        Reservation reservation = findById(idReservation);

        Client client = clientRepository.findByNomAndPrenom(nom, prenom)
                .orElseThrow(() ->
                        new IllegalArgumentException("Client introuvable"));

        reservation.setClient(client);

        Reservation saved = reservationRepository.save(reservation);

        return reservationMapper.toDto(saved);
    }

    // 2. Créer une réservation et son contrat associé
    @Override
    public ReservationResponseDto creerReservationEtContratAssocie(
            ReservationWithContratRequestDTO requestDTO) {

        if (requestDTO == null
                || requestDTO.dateSignature() == null
                || requestDTO.dateDebut() == null
                || requestDTO.dateFin() == null
                || requestDTO.statut() == null
                || requestDTO.vehiculeId() == null
                || requestDTO.clientId() == null) {
            throw new IllegalArgumentException(
                    "Toutes les données de réservation et de contrat sont obligatoires.");
        }

        if (requestDTO.dateFin().isBefore(requestDTO.dateDebut())) {
            throw new IllegalArgumentException(
                    "La date de fin doit être après la date de début.");
        }

        Client client = clientRepository.findById(requestDTO.clientId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client",
                                requestDTO.clientId()));

        Vehicule vehicule = vehiculeRepository.findById(requestDTO.vehiculeId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Véhicule",
                                requestDTO.vehiculeId()));

        if (vehicule.getStatut() != StatutVehicule.DISPONIBLE) {
            throw new IllegalArgumentException(
                    "Le véhicule n'est pas disponible.");
        }

        Reservation reservation = new Reservation();

        reservation.setDateDebut(requestDTO.dateDebut());
        reservation.setDateFin(requestDTO.dateFin());
        reservation.setStatut(requestDTO.statut());
        reservation.setClient(client);
        reservation.setVehicule(vehicule);

        Reservation savedReservation =
                reservationRepository.save(reservation);

        long nombreJours = ChronoUnit.DAYS.between(
                requestDTO.dateDebut(), requestDTO.dateFin());

        BigDecimal montantTotal = vehicule.getTarifJournalier()
                .multiply(BigDecimal.valueOf(nombreJours));

        Contrat contrat = new Contrat();

        contrat.setDateSignature(requestDTO.dateSignature());
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(true);
        contrat.setReservation(savedReservation);

        contratRepository.save(contrat);
        savedReservation.setContrat(contrat);

        return reservationMapper.toDto(savedReservation);
    }

    private void validate(Reservation reservation) {

        if (reservation == null
                || reservation.getDateDebut() == null
                || reservation.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "Les dates de réservation sont obligatoires.");
        }

        if (reservation.getStatut() == null) {
            throw new IllegalArgumentException(
                    "Le statut de réservation est obligatoire.");
        }

        if (reservation.getDateFin().isBefore(
                reservation.getDateDebut())) {
            throw new IllegalArgumentException(
                    "Date de fin invalide.");
        }
    }

    private void resolveAssociations(Reservation reservation) {
        reservation.setClient(findClient(reservation));
        reservation.setVehicule(findVehicule(reservation));
    }

    private Client findClient(Reservation reservation) {

        if (reservation.getClient() == null
                || reservation.getClient().getIdClient() == null) {
            throw new IllegalArgumentException("Client obligatoire.");
        }

        Long id = reservation.getClient().getIdClient();

        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Client", id));
    }

    private Vehicule findVehicule(Reservation reservation) {

        if (reservation.getVehicule() == null
                || reservation.getVehicule().getIdVehicule() == null) {
            throw new IllegalArgumentException("Véhicule obligatoire.");
        }

        Long id = reservation.getVehicule().getIdVehicule();

        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Véhicule", id));
    }
}
