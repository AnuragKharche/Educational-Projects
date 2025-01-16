package acb54.eparkingsolution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.eparkingsolution.controller.BookingController;
import com.eparkingsolution.model.Card;
import com.eparkingsolution.model.ParkingSpace;
import com.eparkingsolution.model.Transaction;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.CardRepository;
import com.eparkingsolution.repository.ParkingSpaceRepository;
import com.eparkingsolution.repository.TransactionRepository;
import com.eparkingsolution.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParkingSpaceRepository parkingSpaceRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CardRepository cardRepository;

    @MockBean
    private UserRepository userRepository;

    private ParkingSpace parkingSpace;
    private Card card;
    private User user;

    @BeforeEach
    public void setUp() {
        parkingSpace = new ParkingSpace();
        parkingSpace.setId_ps(1L);
        parkingSpace.setPrice(2.0f);

        card = new Card();
        card.setId(1L);
        card.setCardNumber("1234567890123456");
        card.setAccepted(true);

        user = new User();
        user.setId(1);
        user.setUsername("testUser");
    }

    @Test
    public void testBookParkingSpace() throws Exception {
        when(parkingSpaceRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(parkingSpace));
        when(transactionRepository.findByParkingSpace(any(ParkingSpace.class))).thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(post("/booking/parking-space/{id_ps}", 1L)
                        .param("startDate", "2023-05-10")
                        .param("startTime", "10:00")
                        .param("endDate", "2023-05-10")
                        .param("endTime", "11:00")
                        .param("licensePlate", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("totalCost"))
                .andExpect(model().attributeExists("transaction"))
                .andExpect(view().name("bookingCheckout"));

        verify(parkingSpaceRepository, times(1)).findById(1L);
        verify(transactionRepository, times(1)).findByParkingSpace(parkingSpace);
    }

    @Test
    public void testProcessPayment() throws Exception {
        when(cardRepository.findByCardNumber(any(String.class))).thenReturn(card);
        when(parkingSpaceRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(parkingSpace));
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        mockMvc.perform(post("/payment/{id_ps}", 1L)
                        .param("cardNumber", "1234567890123456")
                        .sessionAttr("startDate", "2023-05-10")
                        .sessionAttr("startTime", "10:00")
                        .sessionAttr("endDate", "2023-05-10")
                        .sessionAttr("endTime", "11:00")
                        .sessionAttr("totalCost", 4.0)
                        .sessionAttr("licensePlate", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("transaction"))
                .andExpect(view().name("displayReceipt"));

        verify(cardRepository, times(1)).findByCardNumber("1234567890123456");
        verify(parkingSpaceRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    public void testProcessPaymentFailed() throws Exception {
        Card declinedCard = new Card();
        declinedCard.setId(2L);
        declinedCard.setCardNumber("9876543210987654");
        declinedCard.setAccepted(false);

        when(cardRepository.findByCardNumber(any(String.class))).thenReturn(declinedCard);
        when(parkingSpaceRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(parkingSpace));
        when(userRepository.findByUsername(any(String.class))).thenReturn(user);

        mockMvc.perform(post("/payment/{id_ps}", 1L)
                        .param("cardNumber", "9876543210987654")
                        .sessionAttr("startDate", "2023-05-10")
                        .sessionAttr("startTime", "10:00")
                        .sessionAttr("endDate", "2023-05-10")
                        .sessionAttr("endTime", "11:00")
                        .sessionAttr("totalCost", 4.0)
                        .sessionAttr("licensePlate", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("paymentError"));

        verify(cardRepository, times(1)).findByCardNumber("9876543210987654");
        verify(parkingSpaceRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findByUsername("testUser");
    }
}
