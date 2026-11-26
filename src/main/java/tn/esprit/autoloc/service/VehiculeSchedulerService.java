package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.StatutReservation;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.IReservationRepository;
import tn.esprit.autoloc.repository.IVehiculeRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehiculeSchedulerService {

    private final IReservationRepository reservationRepository;
    private final IVehiculeRepository vehiculeRepository;

    @Scheduled(cron = "0 0 1 * * *", zone = "Africa/Lagos")
    @Transactional
    public void terminerReservationsEchues() {
        LocalDate aujourdHui = LocalDate.now(ZoneId.of("Africa/Lagos"));
        List<Reservation> reservations = reservationRepository
                .findByDateFinLessThanEqualAndStatutNot(aujourdHui, StatutReservation.TERMINEE);

        for (Reservation reservation : reservations) {
            reservation.setStatut(StatutReservation.TERMINEE);
            reservationRepository.save(reservation);
            log.info("Réservation {} terminée (date de fin : {}).",
                    reservation.getIdReservation(), reservation.getDateFin());

            Vehicule vehicule = reservation.getVehicule();
            boolean autreLocationConfirmeeActive = reservationRepository
                    .existsByVehicule_IdVehiculeAndIdReservationNotAndStatutAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(
                            vehicule.getIdVehicule(),
                            reservation.getIdReservation(),
                            StatutReservation.CONFIRMEE,
                            aujourdHui,
                            aujourdHui);

            // Une maintenance en cours ne doit pas être annulée par la fin d'une réservation.
            if (!autreLocationConfirmeeActive
                    && vehicule.getStatut() != StatutVehicule.MAINTENANCE) {
                if (vehicule.getStatut() != StatutVehicule.DISPONIBLE) {
                    vehicule.setStatut(StatutVehicule.DISPONIBLE);
                    vehiculeRepository.save(vehicule);
                    log.info("Véhicule {} libéré et marqué disponible.",
                            vehicule.getImmatriculation());
                }
            } else if (autreLocationConfirmeeActive) {
                log.info("Véhicule {} conservé comme loué : une autre réservation confirmée est active.",
                        vehicule.getImmatriculation());
            }
        }
    }
}
