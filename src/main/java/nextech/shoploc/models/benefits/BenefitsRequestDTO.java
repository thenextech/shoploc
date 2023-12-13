package nextech.shoploc.models.benefits;

import lombok.Data;
import nextech.shoploc.domains.enums.TypeBenefits;

import java.time.LocalDateTime;

@Data
public class BenefitsRequestDTO {

    private Long benefitsId;
    private String description;
    private double cost;
    private TypeBenefits typeBenefits;
    private LocalDateTime dateCreation;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
    private Long productId;
    private Long userId;
}
