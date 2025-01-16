package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.eparkingsolution.controller.DriverViewBookingController;
import com.eparkingsolution.model.Message;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MessageRepository;
import com.eparkingsolution.repository.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class DriverMessageTest {

    @InjectMocks
    private DriverViewBookingController driverViewBookingController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    private MockMvc mockMvc;

    @Test
    void sendMessageTest() throws Exception {
        // Prepare the test data and mock the required methods
        User sender = new User();
        sender.setId(1);
        sender.setUsername("sender");

        User receiver = new User();
        receiver.setId(2);
        receiver.setUsername("receiver");

        when(userRepository.findByUsername(any())).thenReturn(sender);
        when(userRepository.findById((Long) any())).thenReturn(java.util.Optional.of(receiver));
        when(messageRepository.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));

        mockMvc = MockMvcBuilders.standaloneSetup(driverViewBookingController).build();
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // Perform the request and verify the result
        mockMvc.perform(post("/sendMessage/{userId}", 2)
                        .param("recipient", "receiver")
                        .param("subject", "Test Subject")
                        .param("message", "Test Message"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/myBookings"))
                .andExpect(flash().attribute("messageSent", "Message was sent"));

        // Verify the interactions with the mocked objects
        verify(userRepository, times(1)).findByUsername(any());
        verify(userRepository, times(1)).findById((Long) any());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

}
