package nextech.shoploc.services.client;

import nextech.shoploc.domains.Client;
import nextech.shoploc.models.client.ClientRequestDTO;
import nextech.shoploc.models.client.ClientResponseDTO;
import nextech.shoploc.repositories.ClientRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
    private final ClientRepository clientRepository;
    private final ModelMapperUtils modelMapperUtils;

    public ClientServiceImpl(ClientRepository clientRepository, ModelMapperUtils modelMapperUtils) {
        this.clientRepository = clientRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public ClientResponseDTO createClient(ClientRequestDTO clientRequestDTO) {
        Client client = modelMapperUtils.getModelMapper().map(clientRequestDTO, Client.class);
        String encodedPassword = passwordEncoder.encode(client.getPassword());
        client.setPassword(encodedPassword);
        client = clientRepository.save(client);
        return modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class);
    }

    @Override
    public ClientResponseDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class);
    }

    @Override
    public ClientResponseDTO getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));
        return modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class);
    }
    
    @Override
    public ClientResponseDTO loginClient(String email, String password) {
    	Client client = clientRepository.findByEmail(email).orElse(null);
        if (client != null && passwordEncoder.matches(password, client.getPassword())) {
            return modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class);
        } else {
            return null;
        }
    }

    @Override
    public List<ClientResponseDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(client -> modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client not found with ID: " + id);
        }
        clientRepository.deleteById(id);
    }

    @Override
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO clientRequestDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(clientRequestDTO, client);
        client = clientRepository.save(client);
        return modelMapperUtils.getModelMapper().map(client, ClientResponseDTO.class);
    }
}
