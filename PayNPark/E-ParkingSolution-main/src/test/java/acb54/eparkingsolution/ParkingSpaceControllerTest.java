package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.repository.CarParkRepository;
import com.eparkingsolution.repository.ParkingSpaceRepository;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ParkingSpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private CarParkRepository carParkRepository;

    @WithMockUser(username = "testuser")
    @Test
    public void testSavePS() throws Exception {
        CarPark carPark = new CarPark();
        carPark.setName("Test Car Park");
        carPark.setAddress("123 Test St.");
        carPark.setDisabledSpaces("4");
        carParkRepository.save(carPark);

        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setDisabledSpace("true");
        parkingSpace.setFloorLevel("1");
        parkingSpace.setPrice(20);

        mockMvc.perform(MockMvcRequestBuilders.post("/savePS")
                        .flashAttr("parkingSpace", parkingSpace)
                        .param("carPark", String.valueOf(carPark.getId_cp()))
                        .param("quantity", "3"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/manageCarPark"));

        Optional<CarPark> savedCarPark = carParkRepository.findById(carPark.getId_cp());
        Optional<ParkingSpace> savedParkingSpace = parkingSpaceRepository.findById(parkingSpace.getId_ps());

        // Add assertions to check that the parking spaces were saved correctly
        // For example:
        // assertEquals(3, savedCarPark.get().getParkingSpace().size());
        // assertEquals(carPark.getIdCp(), savedParkingSpace.get().getCarPark().getIdCp());
    }
}
