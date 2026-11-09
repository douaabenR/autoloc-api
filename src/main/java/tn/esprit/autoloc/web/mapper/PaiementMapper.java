package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.web.dto.PaiementResponseDto;

@Component
public class PaiementMapper {
    public PaiementResponseDto toDto(Paiement paiement) {
        return new PaiementResponseDto(paiement.getIdPaiement(), paiement.getMontant(), paiement.getDatePaiement(),
                paiement.getModePaiement(), paiement.getContrat() == null ? null : paiement.getContrat().getIdContrat());
    }
}
