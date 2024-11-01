
package app.webservice.tracker;

import app.domain.tracker.AppRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import app.domain.tracker.RequestService;
import lombok.val;
import org.springframework.web.client.RestTemplate;
import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ContextConfiguration(classes = RequestControllerConfiguration.class)
@WebMvcTest(controllers = RequestController.class)
@AutoConfigureMockMvc
@ImportAutoConfiguration(AopAutoConfiguration.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RequestService requestService;

    @MockBean
    private RestTemplate restTemplate;

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
        val endpoint = "https://127.0.0.1/verve/accept";
        val header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);

        val responseEntity = new ResponseEntity<>(
                "qwe",
                header,
                HttpStatus.OK
        );

        //when
        when(restTemplate.exchange(
                eq(endpoint),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(String.class))
        ).thenReturn(responseEntity);

        // then
        mockMvc.perform(
                        get("/verve/accept")
                                .param("id", requestId)
                                .param("endpoint", endpoint)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));


        verify(requestService).saveRequest(any(AppRequest.class));
        verify(requestService).countRequests(any(ZonedDateTime.class));
    }

    @Test
    void whenGetAcceptWithMalformed_ThenStatus400() throws Exception {
        mockMvc.perform(
                        get("/verve/accept")
                                .param("endpoint", "blub")
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    void whenPostAccept_ThenStatus200() throws Exception {
        // setup
        val postAccept = new app.verve.model.PostAcceptRequest();
        postAccept.setId(32);

        // then
        mockMvc.perform(
                        post("/verve/accept")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postAccept))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("ok"));


        verify(requestService).saveRequest(any(AppRequest.class));
    }

}

