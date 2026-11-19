package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IContratRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.mapper.ContratMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final IContratRepository contratRepository;
    private final IReservationRepository reservationRepository;
    private final ContratMapper contratMapper;

    @Override
    public Contrat create(Contrat contrat) {
        validate(contrat);
        if (contrat.getIdContrat() != null) {
            throw new IllegalArgumentException("Un nouveau contrat ne doit pas avoir d'identifiant.");
        }
        contrat.setReservation(findReservation(contrat.getReservation()));
        if (contrat.getPaiements() != null) {
            contrat.getPaiements().forEach(paiement -> paiement.setContrat(contrat));
        }
        return contratRepository.save(contrat);
    }

    @Override
    public Contrat findById(Long id) {
        return contratRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat", id));
    }

    @Override
    public List<Contrat> findAll() {
        return contratRepository.findAll();
    }

    @Override
    public Contrat update(Long id, Contrat contrat) {
        validate(contrat);
        Contrat existing = findById(id);
        existing.setDateSignature(contrat.getDateSignature());
        existing.setMontantTotal(contrat.getMontantTotal());
        existing.setValide(contrat.isValide());
        existing.setReservation(findReservation(contrat.getReservation()));
        return contratRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        if (!contratRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contrat", id);
        }
        contratRepository.deleteById(id);
    }

    @Override
    public ContratResponseDto affecterContratAReservation(
            LocalDate dateSignature, LocalDate dateDebut, LocalDate dateFin) {
        if (dateSignature == null || dateDebut == null || dateFin == null) {
            throw new IllegalArgumentException(
                    "Les dates de signature et de réservation sont obligatoires.");
        }
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException(
                    "La date de fin doit être égale ou postérieure à la date de début.");
        }

        Reservation reservation = reservationRepository
                .findFirstByDateDebutAndDateFin(dateDebut, dateFin)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Réservation", dateDebut + " - " + dateFin));

        if (reservation.getContrat() != null) {
            throw new IllegalArgumentException(
                    "Cette réservation possède déjà un contrat.");
        }

        long nombreJours = ChronoUnit.DAYS.between(dateDebut, dateFin);
        BigDecimal montantTotal = reservation.getVehicule().getTarifJournalier()
                .multiply(BigDecimal.valueOf(nombreJours));

        Contrat contrat = new Contrat();
        contrat.setDateSignature(dateSignature);
        contrat.setMontantTotal(montantTotal);
        contrat.setValide(true);
        contrat.setReservation(reservation);

        Contrat saved = contratRepository.save(contrat);
        reservation.setContrat(saved);
        return contratMapper.toDto(saved);
    }

    private void validate(Contrat contrat) {
        if (contrat == null) {
            throw new IllegalArgumentException("Le contrat est obligatoire.");
        }
        BigDecimal montant = contrat.getMontantTotal();
        if (montant == null || montant.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le montant total du contrat doit être positif ou nul.");
        }
    }

    private Reservation findReservation(Reservation reservation) {
        if (reservation == null || reservation.getIdReservation() == null) {
            throw new IllegalArgumentException("L'identifiant d'une réservation existante est obligatoire.");
        }
        Long reservationId = reservation.getIdReservation();
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", reservationId));
    }
}
