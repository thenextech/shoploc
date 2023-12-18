package nextech.shoploc.controllers.crud;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import nextech.shoploc.domains.Invoice;
import nextech.shoploc.models.invoice.InvoiceRequestDTO;
import nextech.shoploc.models.invoice.InvoiceResponseDTO;
import nextech.shoploc.services.auth.EmailSenderService;
import nextech.shoploc.services.invoice.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
@Api(tags = "Invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final EmailSenderService emailSenderService;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService, final EmailSenderService emailSenderService) {
        this.invoiceService = invoiceService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Create an invoice", notes = "Creates a new invoice")
    public ResponseEntity<InvoiceResponseDTO> createInvoice(@RequestBody InvoiceRequestDTO invoiceRequestDTO) throws Exception {
        InvoiceResponseDTO createdInvoice = invoiceService.createInvoice(invoiceRequestDTO);
        Invoice invoice1 = invoiceService.convertResponseToEntity(createdInvoice);
        if (createdInvoice != null) {
            emailSenderService.sendInvoiceEmail(invoice1);
            return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get invoice by ID", notes = "Retrieve an invoice by its ID")
    public ResponseEntity<InvoiceResponseDTO> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDTO invoiceResponseDTO = invoiceService.getInvoiceById(id);
        if (invoiceResponseDTO != null) {
            return new ResponseEntity<>(invoiceResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Get all Invoices")
    public ResponseEntity<List<InvoiceResponseDTO>> getAllInvoices() {
        List<InvoiceResponseDTO> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update an invoice", notes = "Update an existing invoice by its ID")
    public ResponseEntity<InvoiceResponseDTO> updateInvoice(@PathVariable Long id, @RequestBody InvoiceRequestDTO invoiceRequestDTO) {
        Optional<InvoiceResponseDTO> updatedInvoice = Optional.ofNullable(invoiceService.updateInvoice(id, invoiceRequestDTO));
        return updatedInvoice.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete an invoice", notes = "Delete an invoice by its ID")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }

}
