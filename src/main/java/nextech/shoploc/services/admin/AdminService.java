package nextech.shoploc.services.admin;

import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;

import java.util.List;
import java.util.Map;

public interface AdminService {
    AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO);
    AdminResponseDTO getAdminById(Long id);
    AdminResponseDTO getAdminByEmail(String email);
    List<AdminResponseDTO> getAllAdmins();

    void deleteAdmin(Long id);

    AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminRequestDTO);

    Map<String, Object> getSalesStatistics(String startDate, String endDate);
}