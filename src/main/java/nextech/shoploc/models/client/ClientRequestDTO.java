package nextech.shoploc.models.client;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;
import nextech.shoploc.models.user.UserResponseDTO;

@Getter
@Setter
public class ClientRequestDTO extends UserResponseDTO {
    private String licensePlate;
    private boolean isVFP;
    private String lineAddress1;
    private String lineAddress2;
    private String city;
    private String postalCode;
    private Status status;
}