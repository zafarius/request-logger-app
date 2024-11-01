
package app.webservice.tracker;

import app.domain.tracker.AppRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import app.domain.tracker.RequestService;
import lombok.val;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ContextConfiguration(classes = RequestControllerConfiguration.class)
@WebMvcTest(controllers = RequestController.class)
@AutoConfigureMockMvc
@ImportAutoConfiguration(AopAutoConfiguration.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Test
    void whenGetAccept_ThenStatus200() throws Exception {
        // setup
        val requestId = "12345";


        // then
        mockMvc.perform(
                get("/verve/accept").param("id", requestId)
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string("ok"));


        verify(requestService).saveRequest(any(AppRequest.class));
    }

    @Test
    void whenGetAcceptWithEndpoint_ThenStatus200() throws Exception {
        // setup
        val requestId = "12345";


        // then
        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", requestId)
                                .param("endpoint", "blub")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));


        verify(requestService).saveRequest(any(AppRequest.class));
    }

    @Test
    void whenGetAcceptWithMalformed_ThenStatus400() throws Exception {
        // setup
        val requestId = "12345";

        // then
        mockMvc.perform(
                        get("/verve/accept")
                                .param("endpoint", "blub")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}

