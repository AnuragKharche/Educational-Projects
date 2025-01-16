package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MyUserDetails;
import com.eparkingsolution.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CarParkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @WithMockUser(username = "testuser")
    @Test
    public void testSaveCarPark() throws Exception {
        User user = userRepository.getUserByUsername("driver");
        CarPark carPark = new CarPark();
        carPark.setName("Test Car Park");
        carPark.setAddress("123 Main St");
        carPark.setDisabledSpaces("Yes");
        carPark.setUser(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/saveCP")
                        .flashAttr("carpark", carPark))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/manageCarPark"));
    }
}

