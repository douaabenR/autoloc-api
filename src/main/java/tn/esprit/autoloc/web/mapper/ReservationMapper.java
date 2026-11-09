package tn.esprit.autoloc.web.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.web.dto.ReservationRequestDto;
import tn.esprit.autoloc.web.dto.ReservationResponseDto;

@Component
public class ReservationMapper {
    public Reservation toEntity(ReservationRequestDto dto) {
        Client client = new Client();
        client.setIdClient(dto.clientId());
        Vehicule vehicule = new Vehicule();
        vehicule.setIdVehicule(dto.vehiculeId());
        return new Reservation(null, dto.dateDebut(), dto.dateFin(), dto.statut(), vehicule, client, null);
    }

    public ReservationResponseDto toDto(Reservation reservation) {
        return new ReservationResponseDto(reservation.getIdReservation(), reservation.getDateDebut(),
                reservation.getDateFin(), reservation.getStatut(),
                reservation.getVehicule() == null ? null : reservation.getVehicule().getIdVehicule(),
                reservation.getClient() == null ? null : reservation.getClient().getIdClient());
    }
}
