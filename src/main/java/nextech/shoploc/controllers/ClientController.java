package nextech.shoploc.controllers;

import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.services.client.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {
        List<ClientResponseDTO> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        ClientResponseDTO client = clientService.getClientById(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO client = clientService.createClient(clientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable Long id, @RequestBody ClientRequestDTO clientRequestDTO) {
        ClientResponseDTO updatedClient = clientService.updateClient(id, clientRequestDTO);
        if (updatedClient != null) {
            return ResponseEntity.ok(updatedClient);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
