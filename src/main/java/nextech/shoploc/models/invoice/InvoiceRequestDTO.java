package nextech.shoploc.models.invoice;

import lombok.Getter;
import lombok.Setter;
import nextech.shoploc.domains.enums.PaymentMethod;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvoiceRequestDTO {
    private double priceInvoice;
    private LocalDateTime dateInvoice;
    private PaymentMethod paymentMethod;
    private Long orderId;
}
