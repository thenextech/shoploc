package nextech.shoploc.models.merchant;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.AccountStatus;
import nextech.shoploc.models.user.UserResponseDTO;

@Getter
@Setter
public class MerchantRequestDTO extends UserResponseDTO {
    private String businessName;
    private String address;
    private String phoneNumber;
    private AccountStatus status;
}

