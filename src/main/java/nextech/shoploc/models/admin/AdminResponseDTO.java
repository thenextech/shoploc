package nextech.shoploc.models.admin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDTO extends AdminRequestDTO {
    private Long userId;

}