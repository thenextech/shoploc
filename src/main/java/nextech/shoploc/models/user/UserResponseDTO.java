package nextech.shoploc.models.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO extends UserRequestDTO {
    private Long userId;
}