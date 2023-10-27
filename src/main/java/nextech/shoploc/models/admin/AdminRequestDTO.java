package nextech.shoploc.models.admin;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.models.user.UserRequestDTO;
@Getter
@Setter
public class AdminRequestDTO extends UserRequestDTO {
    private boolean isActive;
}