package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.web.dto.ContratRequestDto;
import tn.esprit.autoloc.web.dto.ContratResponseDto;
import tn.esprit.autoloc.web.dto.PaiementRequestDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContratMapper {
    public Contrat toEntity(ContratRequestDto dto) {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(dto.reservationId());
        Contrat contrat = new Contrat();
        contrat.setDateSignature(dto.dateSignature());
        contrat.setMontantTotal(dto.montantTotal());
        contrat.setValide(dto.valide());
        contrat.setReservation(reservation);
        if (dto.paiements() != null) {
            List<Paiement> paiements = new ArrayList<>(dto.paiements().stream().map(this::toPaiement).toList());
            paiements.forEach(paiement -> paiement.setContrat(contrat));
            contrat.setPaiements(paiements);
        }
        return contrat;
    }

    public ContratResponseDto toDto(Contrat contrat) {
        return new ContratResponseDto(contrat.getIdContrat(), contrat.getDateSignature(), contrat.getMontantTotal(),
                contrat.isValide(), contrat.getReservation() == null ? null : contrat.getReservation().getIdReservation(),
                contrat.getPaiements() == null ? List.of() : contrat.getPaiements().stream()
                        .map(Paiement::getIdPaiement).toList());
    }

    private Paiement toPaiement(PaiementRequestDto dto) {
        Paiement paiement = new Paiement();
        paiement.setMontant(dto.montant());
        paiement.setDatePaiement(dto.datePaiement());
        paiement.setModePaiement(dto.modePaiement());
        return paiement;
    }
}
