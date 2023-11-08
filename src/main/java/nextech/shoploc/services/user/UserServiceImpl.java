package nextech.shoploc.services.user;

import nextech.shoploc.domains.User;
import nextech.shoploc.models.user.UserRequestDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import nextech.shoploc.repositories.UserRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapperUtils modelMapperUtils;
    
	@Autowired
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapperUtils modelMapperUtils) {
        this.userRepository = userRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User user = modelMapperUtils.getModelMapper().map(userRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return modelMapperUtils.getModelMapper().map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> modelMapperUtils.getModelMapper().map(value, UserResponseDTO.class))
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.map(value -> modelMapperUtils.getModelMapper().map(value, UserResponseDTO.class))
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapperUtils.getModelMapper().map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> {
            modelMapperUtils.getModelMapper().map(userRequestDTO, user);
            user = userRepository.save(user);
            return modelMapperUtils.getModelMapper().map(user, UserResponseDTO.class);
        }).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updatePassword(Long id, String newPassword) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> {
            user.setPassword(newPassword);
            user = userRepository.save(user);
            return modelMapperUtils.getModelMapper().map(user, UserResponseDTO.class);
        }).orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
    }

    @Override
    public boolean verifyPassword(String rawPassword, String encodedPassword) {

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


}
