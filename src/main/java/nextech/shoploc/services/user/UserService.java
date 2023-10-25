package nextech.shoploc.services.user;

import nextech.shoploc.models.user.UserRequestDTO;
import nextech.shoploc.models.user.UserResponseDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO userRequestDTO);
    UserResponseDTO getUserById(Long id);
    UserResponseDTO getUserByEmail(String email);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO);
    void deleteUser(Long id);
    UserResponseDTO updatePassword(Long id, String newPassword);
}
