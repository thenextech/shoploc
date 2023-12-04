package nextech.shoploc.services.invoice;

import nextech.shoploc.domains.Invoice;
import nextech.shoploc.domains.Order;
import nextech.shoploc.models.invoice.InvoiceRequestDTO;
import nextech.shoploc.models.invoice.InvoiceResponseDTO;
import nextech.shoploc.repositories.InvoiceRepository;
import nextech.shoploc.repositories.OrderRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.modelmapper.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    private final ModelMapperUtils modelMapperUtils;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, ModelMapperUtils modelMapperUtils, OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.orderRepository = orderRepository;

        // Mapper & Converter
        Converter<Long, Order> convertIdentifierToOrder = context -> this.orderRepository.findById(context.getSource())
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + context.getSource()));


        this.modelMapperUtils.getModelMapper().typeMap(InvoiceRequestDTO.class, Invoice.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToOrder)
                        .map(InvoiceRequestDTO::getOrderId, Invoice::setOrder));

        this.modelMapperUtils.getModelMapper().typeMap(Invoice.class, InvoiceResponseDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getOrder().getOrderId(), InvoiceResponseDTO::setOrderId));


        this.modelMapperUtils.getModelMapper().typeMap(InvoiceResponseDTO.class, Invoice.class)
                .addMappings(mapper -> mapper.when(ctx -> ctx.getSource() != null)
                        .using(convertIdentifierToOrder)
                        .map(InvoiceResponseDTO::getOrderId, Invoice::setOrder));

    }

    @Override
    public InvoiceResponseDTO createInvoice(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = modelMapperUtils.getModelMapper().map(invoiceRequestDTO, Invoice.class);
        invoice = invoiceRepository.save(invoice);
        return modelMapperUtils.getModelMapper().map(invoice, InvoiceResponseDTO.class);
    }

    @Override
    public InvoiceResponseDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(invoice, InvoiceResponseDTO.class);
    }

    @Override
    public List<InvoiceResponseDTO> getAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(invoice -> modelMapperUtils.getModelMapper().map(invoice, InvoiceResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvoice(Long id) {
        if (!invoiceRepository.existsById(id)) {
            throw new NotFoundException("Invoice not found with ID: " + id);
        }
        invoiceRepository.deleteById(id);
    }

    @Override
    public InvoiceResponseDTO updateInvoice(Long id, InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(invoiceRequestDTO, invoice);
        invoice = invoiceRepository.save(invoice);
        return modelMapperUtils.getModelMapper().map(invoice, InvoiceResponseDTO.class);
    }

    @Override
    public Invoice convertResponseToEntity(InvoiceResponseDTO invoiceResponseDTO) {
        return modelMapperUtils.getModelMapper().map(invoiceResponseDTO, Invoice.class);
    }

}
