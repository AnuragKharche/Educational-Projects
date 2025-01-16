package acb54.eparkingsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eparkingsolution.controller.BookingController;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UpdateAddressTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
    }

    @Test
    public void updateAddressTest() throws Exception {
        // Given
        String address = "123 Test Street";
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setAddress(address);

        Principal principal = () -> username;

        // When
        when(userRepository.findByUsername(principal.getName())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Then
        mockMvc.perform(post("/update-address")
                        .principal(principal)
                        .param("address", address))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/booking"))
                .andExpect(flash().attribute("message", "Address updated successfully"));
    }
}

