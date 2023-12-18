package nextech.shoploc.services.invoice;

import nextech.shoploc.domains.Invoice;
import nextech.shoploc.models.invoice.InvoiceRequestDTO;
import nextech.shoploc.models.invoice.InvoiceResponseDTO;

import java.util.List;

public interface InvoiceService {
    InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO);

    InvoiceResponseDTO getInvoiceById(Long id);

    List<InvoiceResponseDTO> getAllInvoices();

    void deleteInvoice(Long id);

    InvoiceResponseDTO updateInvoice(Long id, InvoiceRequestDTO invoiceRequestDTO);

    Invoice convertResponseToEntity(InvoiceResponseDTO invoiceResponseDTO);
}
