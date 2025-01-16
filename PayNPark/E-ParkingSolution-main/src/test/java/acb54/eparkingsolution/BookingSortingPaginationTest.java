package acb54.eparkingsolution;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.eparkingsolution.controller.BookingController;
import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.UserRepository;
import com.eparkingsolution.service.CarParkService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class BookingSortingPaginationTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private CarParkService carParkService;

    @InjectMocks
    private BookingController bookingController;

    private MockMvc mockMvc;

    private Principal principal;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        principal = mock(Principal.class);
    }

    @Test
    public void testViewHomePage() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setAddress("123 Main St");

        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        mockMvc.perform(get("/booking")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("address", "123 Main St"))
                .andExpect(view().name("booking"));
    }

    @Test
    public void testFindPaginated() throws Exception {
        int pageNo = 1;
        int pageSize = 5;
        String sortField = "name";
        String sortDir = "asc";

        User user = new User();
        user.setUsername("testuser");
        user.setAddress("123 Main St");

        CarPark carPark1 = new CarPark();
        carPark1.setName("CarPark1");
        carPark1.setAddress("Address1");
        carPark1.setDistanceInMiles(1.0);

        CarPark carPark2 = new CarPark();
        carPark2.setName("CarPark2");
        carPark2.setAddress("Address2");
        carPark2.setDistanceInMiles(2.0);

        List<CarPark> carParks = Arrays.asList(carPark1, carPark2);

        Page<CarPark> page = new PageImpl<>(carParks, PageRequest.of(pageNo - 1, pageSize), carParks.size());

        when(principal.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(user);
        when(carParkService.findPaginated(pageNo, pageSize, sortField, sortDir)).thenReturn(page);

        mockMvc.perform(get("/page/{pageNo}", pageNo)
                        .param("sortField", sortField)
                        .param("sortDir", sortDir)
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("address", "123 Main St"))
                .andExpect(model().attribute("currentPage", pageNo))
                .andExpect(model().attribute("totalPages", page.getTotalPages()))
                .andExpect(model().attribute("totalItems", page.getTotalElements()))
                .andExpect(model().attribute("sortField", sortField))
                .andExpect(model().attribute("sortDir", sortDir))
                .andExpect(model().attribute("reverseSortDir", "desc"))
                .andExpect(model().attribute("carPark", hasSize(2)))
                .andExpect(model().attribute("carPark", hasItem(
                        allOf(
                                hasProperty("name", is("CarPark1")),
                                hasProperty("address", is("Address1")),
                                hasProperty("distanceInMiles", is(1.0))
                        )
                )))
                .andExpect(model().attribute("carPark", hasItem(
                        allOf(
                                hasProperty("name", is("CarPark2")),
                                hasProperty("address", is("Address2")),
                                hasProperty("distanceInMiles", is(2.0))
                        )
                )))
                .andExpect(view().name("booking"));
    }
}


