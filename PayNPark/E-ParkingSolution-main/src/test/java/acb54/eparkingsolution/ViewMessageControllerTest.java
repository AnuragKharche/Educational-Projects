package acb54.eparkingsolution;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.eparkingsolution.controller.ViewMessagesController;
import com.eparkingsolution.model.Message;
import com.eparkingsolution.model.User;
import com.eparkingsolution.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ViewMessagesController.class)
public class ViewMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageRepository messageRepository;

    @Test
    public void testSubmitReplyForm() throws Exception {
        // Arrange
        Message originalMessage = new Message();
        originalMessage.setId(1L);
        originalMessage.setSubject("Test subject");
        originalMessage.setSender(new User());
        originalMessage.setReceiver(new User());

        when(messageRepository.findById(1L)).thenReturn(Optional.of(originalMessage));
        when(messageRepository.save(any(Message.class))).thenReturn(new Message());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/parkingOwnerInbox/reply/{messageId}", 1L)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("subject", "Test subject")
                        .param("message", "Test message"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/parkingOwnerInbox"));

        verify(messageRepository, times(1)).findById(1L);
        verify(messageRepository, times(1)).save(any(Message.class));
    }



}
