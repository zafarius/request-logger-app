

package app.repository.tracker;

import app.domain.tracker.AppRequest;
import app.domain.tracker.RequestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import lombok.val;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RequestMapperTest {

    @InjectMocks
    private RequestMapperImpl requestMapper;

    @Test
    public void testMapAppRequestToRequestEntity() {
        // setup
        val created = ZonedDateTime.now();
        val appRequest = new AppRequest(
                1234,
                created,
                RequestHelper.generateCreatedId(created)
        );

        // when
        val requestEntity = requestMapper.map(appRequest);

        // then
        assertThat(appRequest.getRequestId()).isEqualTo(requestEntity.getRequestId());
        assertThat(appRequest.getCreated()).isEqualTo(requestEntity.getCreated());
        assertThat(appRequest.getCreatedId()).isEqualTo(requestEntity.getCreatedId());
    }

    @Test
    public void testMapRequestEntityToAppRequest() {
        // setup
        val created = ZonedDateTime.now();
        val requestEntity = new RequestEntity();
        requestEntity.setRequestId(123);
        requestEntity.setCreatedId(RequestHelper.generateCreatedId(created));
        requestEntity.setCreated(created);
//        requestEntity.setId(UUID.randomUUID());

        // when
        val appRequest = requestMapper.map(requestEntity);

        // then
        assertThat(requestEntity.getRequestId()).isEqualTo(appRequest.getRequestId());
        assertThat(requestEntity.getCreated()).isEqualTo(appRequest.getCreated());
        assertThat(requestEntity.getCreatedId()).isEqualTo(appRequest.getCreatedId());
    }
}


