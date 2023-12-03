package nextech.shoploc.services.admin;

import nextech.shoploc.domains.Admin;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.repositories.AdminRepository;
import nextech.shoploc.utils.ModelMapperUtils;
import nextech.shoploc.utils.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final ModelMapperUtils modelMapperUtils;

    public AdminServiceImpl(AdminRepository adminRepository, ModelMapperUtils modelMapperUtils) {
        this.adminRepository = adminRepository;
        this.modelMapperUtils = modelMapperUtils;
    }

    @Override
    public AdminResponseDTO createAdmin(AdminRequestDTO adminRequestDTO) {
        Admin admin = modelMapperUtils.getModelMapper().map(adminRequestDTO, Admin.class);
        admin = adminRepository.save(admin);
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO getAdminById(Long id) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with ID: " + id));
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public AdminResponseDTO getAdminByEmail(String email) {
        Admin admin = adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new NotFoundException("Admin not found with email: " + email));
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream()
                .map(admin -> modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new NotFoundException("Admin not found with ID: " + id);
        }
        adminRepository.deleteById(id);
    }

    @Override
    public AdminResponseDTO updateAdmin(Long id, AdminRequestDTO adminRequestDTO) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with ID: " + id));

        modelMapperUtils.getModelMapper().map(adminRequestDTO, admin);
        admin = adminRepository.save(admin);
        return modelMapperUtils.getModelMapper().map(admin, AdminResponseDTO.class);
    }
}

