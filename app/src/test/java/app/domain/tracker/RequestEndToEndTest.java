package app.domain.tracker;

import app.Application;
import app.webservice.tracker.TrackerJob;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.awaitility.Durations;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@AutoConfigureMockMvc
public class RequestEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private RequestService requestService;

    @SpyBean
    private TrackerJob trackerJob;

    @Test
    void whenGetAcceptWithEndpoint_ThenStatus200() throws Exception {
        // setup
        val id = "2623624";
        val id2 = "123451";

        // then
        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", id)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));

        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", id)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));

        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", id2)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));
    }

    @Test
    public void reportTrackedRequests() {
        await()
                .atMost(Durations.TWO_MINUTES)
                .untilAsserted(() -> {
                            verify(trackerJob, atLeast(1)).reportTrackedRequests();
                            verify(requestService, times(1)).countRequests(any());
                        }
                );
    }
}
