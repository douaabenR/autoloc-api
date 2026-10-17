package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "maintenance")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idMaintenance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class  Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMaintenance;

    @Column(nullable = false)
    private LocalDate dateDebut;

    // Nullable : une maintenance en cours n'a pas encore de date de fin
    private LocalDate dateFin;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cout;

    @ManyToOne
    private Vehicule vehicule;
}
