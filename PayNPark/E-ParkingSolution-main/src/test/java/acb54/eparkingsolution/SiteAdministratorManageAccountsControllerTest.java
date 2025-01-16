package acb54.eparkingsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.eparkingsolution.controller.SiteAdministratorManageAccountsController;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SiteAdministratorManageAccountsController.class)
public class SiteAdministratorManageAccountsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setAddress("testAddress");
        user.setRole("Parking Owner");
        user.setEnabled(true);
    }

    @Test
    public void testEnableUser() throws Exception {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        mockMvc.perform(post("/enable/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewAccounts"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDisableUser() throws Exception {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        mockMvc.perform(post("/disable/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewAccounts"));

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateAccounts() throws Exception {
        mockMvc.perform(get("/createAccounts"))
                .andExpect(status().isOk())
                .andExpect(view().name("SiteAdministratorCreateAccounts"));
    }

    @Test
    public void testCreateAccountsSubmit() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/createAccounts")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("address", "testAddress"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/createAccounts"));

        verify(userRepository, times(1)).save(any(User.class));
    }
}
