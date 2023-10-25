package nextech.shoploc.services.client;

import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;

import java.util.List;

public interface ClientService {

    ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO);

    ClientResponseDTO getClientById(Long id);

    ClientResponseDTO getClientByEmail(String email);

    List<ClientResponseDTO> getAllClients();

    void deleteClient(Long id);

    ClientResponseDTO updateClient(Long id, ClientRequestDTO clientRequestDTO);
}
