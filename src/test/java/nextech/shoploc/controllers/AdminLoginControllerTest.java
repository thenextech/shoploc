package nextech.shoploc.controllers;

import nextech.shoploc.controllers.auth.AdminLoginController;
import nextech.shoploc.controllers.auth.SessionManager;
import nextech.shoploc.models.admin.AdminRequestDTO;
import nextech.shoploc.models.admin.AdminResponseDTO;
import nextech.shoploc.services.admin.AdminService;
import nextech.shoploc.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminLoginControllerTest {

    @InjectMocks
    private AdminLoginController adminLoginController;

    @Mock
    private AdminService adminService;

    @Mock
    UserService userService;

    @Mock
    SessionManager sessionManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPostRegisterSuccess() {
        AdminRequestDTO adminRequestDTO = new AdminRequestDTO();
        when(adminService.createAdmin(adminRequestDTO)).thenReturn(new AdminResponseDTO());

        String viewName = adminLoginController.register(adminRequestDTO);

        verify(adminService).createAdmin(adminRequestDTO);

        assertEquals("redirect:/admin/login", viewName);
    }
}
