package nextech.shoploc.models.loyalty_card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyCardRequestDTO {

    private LocalDateTime startDateValidity;
    private LocalDateTime endDateValidity;
    private double points;
    private double solde;
    private Status status;
    private Long userId;

}
