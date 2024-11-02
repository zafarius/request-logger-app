package app.webservice.tracker;

import app.domain.tracker.RequestHelper;
import app.verve.model.PostAcceptRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import app.domain.tracker.RequestService;
import java.time.ZonedDateTime;
import lombok.val;
import app.webservice.contracts.tracker.VerveApi;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RequestController implements VerveApi {
    private final RequestService requestService;

    private final RestTemplate restTemplate;


    @Override
    public ResponseEntity<String> getAccept(final Integer id, final String endpoint) {
        val zoneDateTime = ZonedDateTime.now();
        requestService.saveRequest(RequestHelper.createAppRequest(id, zoneDateTime));

        if (endpoint != null) {
            requestEndpoint(zoneDateTime, endpoint);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("ok");
    }

    @Override
    public ResponseEntity<String> postAccept(final PostAcceptRequest postAcceptRequest) {
        val zoneDateTime = ZonedDateTime.now();
        requestService.saveRequest(RequestHelper.createAppRequest(postAcceptRequest.getId(), zoneDateTime));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("ok");
    }

    private void requestEndpoint(final ZonedDateTime zoneDateTime, final String endpoint) {
            val currentRequests = requestService.countRequests(zoneDateTime);
            val postRequest = new PostAcceptRequest(currentRequests);
            val entity = new HttpEntity<PostAcceptRequest>(postRequest);

            try {
                val response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
                log.info("Successfully requested endpoint: {} {}", endpoint, response.getStatusCode());
            } catch (RestClientResponseException e) {
                log.info("Failed requesting endpoint: {} {}", endpoint, e.getStatusCode());
                throw e;
            }
    }
}
