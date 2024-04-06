package nextech.shoploc.models.benefits_client;

import lombok.Data;
import nextech.shoploc.domains.enums.Status;

import java.time.LocalDateTime;

@Data
public class BenefitsClientRequestDTO {

    private LocalDateTime dateCreation;
    private Status status;
    private Long benefitsId;
    private Long clientId;
}
