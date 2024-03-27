package nextech.shoploc.models.category_product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryProductRequestDTO {
    private Long userId;
    private String categoryName;
    private String urlImage;
    
}
