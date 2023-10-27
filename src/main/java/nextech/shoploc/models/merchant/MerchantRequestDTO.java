package nextech.shoploc.models.merchant;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.MerchantStatus;
import nextech.shoploc.models.user.UserRequestDTO;

@Getter
@Setter
public class MerchantRequestDTO extends UserRequestDTO {
    private String businessName;
    private String address;
    private String phoneNumber;
    private MerchantStatus status;
}

