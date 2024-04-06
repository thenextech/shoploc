package nextech.shoploc.models.merchant;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;
import nextech.shoploc.models.user.UserResponseDTO;

@Getter
@Setter
public class MerchantRequestDTO extends UserResponseDTO {
    private String businessName;
    private String address;
    private String city;
    private String postalCode;
    private String phoneNumber;
    private Status status;
    private String urlImage;
}

