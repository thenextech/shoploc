package nextech.shoploc.models.admin;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.models.user.UserResponseDTO;

@Getter
@Setter
public class AdminRequestDTO extends UserResponseDTO {
    private boolean isActive;
}