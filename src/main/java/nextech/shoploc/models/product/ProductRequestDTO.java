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
    @NotNull
    private double price;
    private Integer qteStock;
    private String description;
    private Status status;

}
