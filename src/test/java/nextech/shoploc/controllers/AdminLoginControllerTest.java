package nextech.shoploc.controllers;

import nextech.shoploc.controllers.auth.AdminLoginController;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class AdminLoginControllerTest {

    @InjectMocks
    private AdminLoginController adminLoginController;

    @Mock
    private AdminService adminService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostRegisterSuccess() {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        adminRequestDTO.setEmail("admin@example.com");
        adminRequestDTO.setPassword("password");
        when(adminService.createAdmin(adminRequestDTO)).thenReturn(new AdminResponseDTO());

        ResponseEntity<Map<String, Object>> res = adminLoginController.register(adminRequestDTO);

        System.out.println(res);
        assertEquals(Objects.requireNonNull(res.getBody()).get("url"), "/admin/login");

        assertTrue(true);
    }

}
