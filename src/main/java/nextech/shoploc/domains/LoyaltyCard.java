package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "Loyalty_card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LoyaltyCardId;

    @Column(name = "start_date_validity")
    private LocalDateTime startDateValidity;

    @Column(name = "end_date_validity")
    private LocalDateTime endDateValidity;

    @Column(name = "points_loyalty")
    private double points;

    @Column(name = "solde")
    private double solde;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}
