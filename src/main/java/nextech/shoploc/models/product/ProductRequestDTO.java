package nextech.shoploc.models.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.Status;

@Getter
@Setter
public class ProductRequestDTO {
    @NotNull
    private Long categoryProduct;
    @NotNull
    private String name;
    private String description;
    private Status status;


}
