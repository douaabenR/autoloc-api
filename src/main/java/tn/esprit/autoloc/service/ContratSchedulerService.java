package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.repository.IContratRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContratSchedulerService {

    private final IContratRepository contratRepository;

    @Scheduled(cron = "0 0 7 * * *", zone = "Africa/Lagos")
    @Transactional(readOnly = true)
    public void verifierContratsArrivantAEcheance() {
        ZoneId zone = ZoneId.of("Africa/Lagos");
        LocalDateTime maintenant = LocalDateTime.now(zone);
        LocalDateTime dans48Heures = maintenant.plusHours(48);

        // L'échéance du contrat est la date de fin de sa réservation (type LocalDate).
        List<Contrat> contrats = contratRepository.findByReservation_DateFinBetween(
                maintenant.toLocalDate(), dans48Heures.toLocalDate());

        if (contrats.isEmpty()) {
            log.info("Aucun contrat n'arrive à échéance dans les prochaines 48 heures.");
            return;
        }

        contrats.forEach(contrat -> log.info(
                "Contrat {} : réservation {} arrivant à échéance le {} (maintenant={}, limite={}).",
                contrat.getIdContrat(),
                contrat.getReservation().getIdReservation(),
                contrat.getReservation().getDateFin(),
                maintenant,
                dans48Heures));
    }
}
