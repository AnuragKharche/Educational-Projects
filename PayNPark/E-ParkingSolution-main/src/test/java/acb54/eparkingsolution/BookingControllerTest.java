package acb54.eparkingsolution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import com.eparkingsolution.controller.BookingController;
import com.eparkingsolution.controller.ManageParkingSpaceController;
import com.eparkingsolution.model.CarPark;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.Transaction;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.ParkingSpaceRepository;
import com.eparkingsolution.repository.TransactionRepository;
import com.eparkingsolution.repository.UserRepository;
import com.eparkingsolution.service.CarParkService;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class BookingControllerTest {

    @InjectMocks
    private ManageParkingSpaceController parkingSpaceController;

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private MockMvc mockMvc;

    @Test
    public void testBookParkingSpace() throws Exception {
        // Setup
        WebApplicationContext webApplicationContext = mock(WebApplicationContext.class);
        Model model = mock(Model.class);
        this.mockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceController).build();
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId_ps(1L);
        parkingSpace.setPrice((float) 1.20);
        when(parkingSpaceRepository.findById(1L)).thenReturn(Optional.of(parkingSpace));
        Transaction existingTransaction = new Transaction();
        existingTransaction.setStartDate(LocalDate.now());
        existingTransaction.setStartTime(LocalTime.now());
        existingTransaction.setEndDate(LocalDate.now().plusDays(1));
        existingTransaction.setEndTime(LocalTime.now().plusHours(1));
        List<Transaction> existingTransactions = Collections.singletonList(existingTransaction);
        when(transactionRepository.findByParkingSpace(parkingSpace)).thenReturn(existingTransactions);

        // Test
        mockMvc.perform(MockMvcRequestBuilders.post("/booking/parking-space/1")
                        .param("startDate", LocalDate.now().toString())
                        .param("startTime", LocalTime.now().toString())
                        .param("endDate", LocalDate.now().plusDays(2).toString())
                        .param("endTime", LocalTime.now().plusHours(2).toString())
                        .param("licensePlate", "ABC123")
                        .flashAttr("transaction", new Transaction())
                        .param("model", String.valueOf(model)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bookingCheckout"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalCost"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("startDate"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("startTime"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("endDate"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("endTime"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("licensePlate"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transaction"));

        // Verify
        verify(parkingSpaceRepository).findById(1L);
        verify(transactionRepository).findByParkingSpace(parkingSpace);
        verify(model).addAttribute("totalCost", 20.0);
        verify(model).addAttribute("startDate", LocalDate.now());
        verify(model).addAttribute("startTime", LocalTime.now());
        verify(model).addAttribute("endDate", LocalDate.now().plusDays(2));
        verify(model).addAttribute("endTime", LocalTime.now().plusHours(2));
        verify(model).addAttribute("transaction", any(Transaction.class));
        verify(model).addAttribute("licensePlate", "ABC123");
    }

}
