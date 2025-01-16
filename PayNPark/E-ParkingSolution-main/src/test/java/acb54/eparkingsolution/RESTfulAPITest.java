package acb54.eparkingsolution;

import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eparkingsolution.controller.RESTfulAPI;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(RESTfulAPI.class)
public class RESTfulAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @InjectMocks
    private RESTfulAPI restfulAPI;

    @Test
    public void testGetUsers() throws Exception {
        // Mock the UserRepository's findAll() method to return a list of two users
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setAddress("address1");
        user1.setRole("Parking Owner");
        user1.setEnabled(true);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setAddress("address2");
        user2.setRole("Parking Owner");
        user2.setEnabled(false);

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        // Make a GET request to /api/users/viewAll
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/viewAll")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                // Expect a 200 OK response
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Expect the response body to be a JSON array with two user objects
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].username").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].password").value("password1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].address").value("address1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].role").value("Parking Owner"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].enabled").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].username").value("user2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].password").value("password2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].address").value("address2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].role").value("Parking Owner"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].enabled").value(false));
    }
}