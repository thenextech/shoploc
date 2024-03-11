package nextech.shoploc.domains;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "line_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    private String productName;
    
    private String clientName;

    private int quantity;
    
    private String orderStatus;

    @Column(name = "unit_price")
    private double unitPrice;
    
   @Column(name = "merchant_id", nullable = false)
   private Long merchantId;
}
