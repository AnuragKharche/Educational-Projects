package acb54.eparkingsolution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.eparkingsolution.controller.IndexController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCons() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("indexLandingPage"));
    }

    @Test
    public void testIndexDriver() throws Exception {
        mockMvc.perform(get("/indexDriver"))
                .andExpect(status().isOk())
                .andExpect(view().name("indexDriver"));
    }

    @Test
    public void testIndexParkingOwner() throws Exception {
        mockMvc.perform(get("/indexParkingOwner"))
                .andExpect(status().isOk())
                .andExpect(view().name("indexParkingOwner"));
    }

    @Test
    public void testIndexSiteAdministrator() throws Exception {
        mockMvc.perform(get("/indexSiteAdministrator"))
                .andExpect(status().isOk())
                .andExpect(view().name("indexSiteAdministrator"));
    }
}

