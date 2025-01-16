package acb54.eparkingsolution;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eparkingsolution.model.User;
import com.eparkingsolution.service.UserDetailsServiceImpl;
import com.eparkingsolution.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDetailsServiceImpl userService;

    @Test
    public void testProcessRegistrationForm() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setAddress("123 Main St");

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));

        ArgumentCaptor<String> usernameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> addressCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(userService, Mockito.times(1)).registerUser(
                usernameCaptor.capture(), passwordCaptor.capture(), addressCaptor.capture());

        Assertions.assertEquals(user.getUsername(), usernameCaptor.getValue());
        Assertions.assertNotEquals(user.getPassword(), passwordCaptor.getValue());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches(user.getPassword(), passwordCaptor.getValue()));
        Assertions.assertEquals(user.getAddress(), addressCaptor.getValue());
    }

}
