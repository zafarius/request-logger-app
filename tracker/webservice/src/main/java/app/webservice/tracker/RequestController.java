package app.webservice.tracker;

import app.domain.tracker.RequestHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import app.domain.tracker.RequestService;
import java.time.ZonedDateTime;
import lombok.val;
import app.webservice.contracts.tracker.VerveApi;

@RestController
@RequiredArgsConstructor
public class RequestController implements VerveApi {
    private final RequestService requestService;

    @Override
    public ResponseEntity<String> getAccept(final Integer id, final String endpoint) {
        val zoneDateTime = ZonedDateTime.now();
        requestService.saveRequest(RequestHelper.createAppRequest(id, zoneDateTime));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("ok");
    }
}
