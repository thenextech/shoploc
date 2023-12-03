package nextech.shoploc.models.user;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO extends UserRequestDTO {
    @NotNull
    private Long userId;
}