package app.domain.tracker;

import app.messaging.tracker.CountReceiver;
import app.messaging.tracker.TrackerJob;
import lombok.val;
import org.awaitility.Durations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@AutoConfigureMockMvc
public class TrackerEndToEndTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TrackerJob trackerJob;

    @SpyBean
    private RequestService requestService;

    @SpyBean
    private CountReceiver countReceiver;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

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
                                .param("endpoint", "http://127.0.0.1:8080/verve/accept")
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
    void whenGetAcceptWithEndpointMalFormed_ThenStatus406() throws Exception {
        // setup
        val id = "2623624";

        // then
        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", id)
                                .param("endpoint", "http://blub")
                )
                .andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }


    @Test
    public void testTrackerJobAndMessaging() {
        await("job started and message send")
                .atMost(Durations.TWO_MINUTES)
                .untilAsserted(() -> {
                            verify(trackerJob, atLeast(1)).reportTrackedRequests();
                            verify(requestService, times(1)).countRequests(any());
                        }
                );

/*        await("message consumed")
                .atMost(Durations.TWO_MINUTES)
                .untilAsserted(() -> verify(countReceiver, times(1)).receive(any()));*/
    }
}
