package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.eparkingsolution.controller.ParkingOwnerViewBookingsController;
import com.eparkingsolution.model.Transaction;
import com.eparkingsolution.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ParkingOwnerViewBookingsController.class)
public class EditTransactionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    public void saveEditedTransactionTest() throws Exception {
        long transactionId = 1L;

        Transaction existingTransaction = new Transaction();
        existingTransaction.setId(transactionId);
        existingTransaction.setAmount(10.0);
        existingTransaction.setCardNumber("1234567890123456");
        existingTransaction.setTransactionType("Parking");
        existingTransaction.setStatus("Completed");
        existingTransaction.setReceiptNumber("R123456");
        existingTransaction.setStartDate(LocalDate.of(2023, 5, 3));
        existingTransaction.setStartTime(LocalTime.of(9, 0));
        existingTransaction.setEndDate(LocalDate.of(2023, 5, 3));
        existingTransaction.setEndTime(LocalTime.of(18, 0));
        existingTransaction.setLicensePlate("ABC123");

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(existingTransaction);

        String requestBody = "{\"amount\": 15.0, \"cardNumber\": \"1234567890123456\", \"transactionType\": \"Parking\", \"status\": \"Completed\", \"receiptNumber\": \"R123456\", \"startDate\": \"2023-05-03\", \"startTime\": \"09:00\", \"endDate\": \"2023-05-03\", \"endTime\": \"18:00\", \"licensePlate\": \"ABC123\"}";

        ResultActions resultActions = mockMvc.perform(post("/parkingOwnerBookings/edit/{id}", transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parkingOwnerBookings"))
                .andExpect(flash().attribute("messageEdited", "Transaction successfully edited."));
    }
}

