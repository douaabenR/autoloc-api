package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository reservationRepository;
    private final IClientRepository clientRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Override
    public Reservation create(Reservation reservation) {
        validate(reservation);
        if (reservation.getIdReservation() != null) {
            throw new IllegalArgumentException("Une nouvelle réservation ne doit pas avoir d'identifiant.");
        }
        resolveAssociations(reservation);
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", id));
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

    private void validate(Reservation reservation) {
        if (reservation == null || reservation.getDateDebut() == null || reservation.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin de la réservation sont obligatoires.");
        }
        if (reservation.getStatut() == null) {
            throw new IllegalArgumentException("Le statut de la réservation est obligatoire.");
        }
        if (reservation.getDateFin().isBefore(reservation.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être égale ou postérieure à la date de début.");
        }
    }

    private void resolveAssociations(Reservation reservation) {
        reservation.setClient(findClient(reservation));
        reservation.setVehicule(findVehicule(reservation));
    }

    private Client findClient(Reservation reservation) {
        if (reservation.getClient() == null || reservation.getClient().getIdClient() == null) {
            throw new IllegalArgumentException("Le client est obligatoire. Fournissez client.idClient.");
        }
        Long idClient = reservation.getClient().getIdClient();
        return clientRepository.findById(idClient)
                .orElseThrow(() -> new ResourceNotFoundException("Client", idClient));
    }

    private Vehicule findVehicule(Reservation reservation) {
        if (reservation.getVehicule() == null || reservation.getVehicule().getIdVehicule() == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire. Fournissez vehicule.idVehicule.");
        }
        Long idVehicule = reservation.getVehicule().getIdVehicule();
        return vehiculeRepository.findById(idVehicule)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule", idVehicule));
    }
}
