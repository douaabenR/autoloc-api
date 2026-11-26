package tn.esprit.autoloc.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import tn.esprit.autoloc.domain.Contrat;

import java.time.LocalDate;
import java.util.List;

public interface IContratRepository extends JpaRepository<Contrat, Long> {

    @EntityGraph(attributePaths = "reservation")
    List<Contrat> findByReservation_DateFinBetween(LocalDate dateDebut, LocalDate dateFin);
}
