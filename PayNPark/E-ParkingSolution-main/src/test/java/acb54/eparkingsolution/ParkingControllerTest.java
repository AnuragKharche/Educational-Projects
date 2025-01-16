package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eparkingsolution.controller.ManageParkingSpaceController;
import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.repository.CarParkRepository;
import com.eparkingsolution.repository.ParkingSpaceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingControllerTest {

    @InjectMocks
    private ManageParkingSpaceController parkingController;

    @Mock
    private CarParkRepository carParkRepository;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Test
    void testSavePS() {
        // Given
        int id_cp = 1;
        int quantity = 3;
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setDisabledSpace("No");
        parkingSpace.setFloorLevel("2");
        parkingSpace.setPrice(10.0f);
        CarPark carPark = new CarPark();

        // Set up request context
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // When
        when(carParkRepository.findById(id_cp)).thenReturn(Optional.of(carPark));
        String result = parkingController.savePS(parkingSpace, id_cp, quantity);

        // Then
        verify(carParkRepository, times(1)).findById(id_cp);
        verify(parkingSpaceRepository, times(quantity)).save(any(ParkingSpace.class));
        assertEquals("redirect:/manageCarPark", result);
    }
}

