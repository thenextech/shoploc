package nextech.shoploc.models.client;
import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.models.user.*;
@Getter
@Setter
public class ClientRequestDTO extends UserRequestDTO {
    private String licensePlate;
    private boolean isVFP;
}