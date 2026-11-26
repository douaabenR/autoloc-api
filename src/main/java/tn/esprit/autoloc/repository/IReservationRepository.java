package tn.esprit.autoloc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findFirstByDateDebutAndDateFin(
            LocalDate dateDebut, LocalDate dateFin);

    List<Reservation> findByDateFinLessThanEqualAndStatutNot(
            LocalDate dateFin, StatutReservation statut);

    boolean existsByVehicule_IdVehiculeAndIdReservationNotAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
            Long vehiculeId,
            Long reservationId,
            StatutReservation statut,
            LocalDate dateDebut,
            LocalDate dateFin);
}
