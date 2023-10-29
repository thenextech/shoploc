package nextech.shoploc.models.client;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.models.user.UserRequestDTO;
@Getter
@Setter
public class ClientRequestDTO extends UserRequestDTO {
    private String licensePlate;
    private boolean isVFP;
    private String lineAddress1;
    private String lineAddress2;
    private String city;
    private String postalCode;
}