package nextech.shoploc.services.admin;

import nextech.shoploc.domains.Admin;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO);
    AdminResponseDTO getAdminById(Long id);
    AdminResponseDTO getAdminByEmail(String email);
    List<AdminResponseDTO> getAllAdmins();

    void deleteAdmin(Long id);

    AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminRequestDTO);
    // Autres méthodes de service si nécessaire.
}