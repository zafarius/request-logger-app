
package app.domain.tracker;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZonedDateTime;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private RequestServiceImpl requestService;


    @Test
    public void testSaveRequest()  {
        // setup
        val requestId = 123456;
        val zoneDateTime = ZonedDateTime.now();
        val appRequest = RequestHelper.createAppRequest(requestId, zoneDateTime);

        // when
       when(requestRepository.requestIdAndCreatedIdExists(appRequest)).thenReturn(false);
       requestService.saveRequest(appRequest);

       // then
       verify(requestRepository).requestIdAndCreatedIdExists(appRequest);
       verify(requestRepository).save(appRequest);
    }

    @Test
    public void testSaveRequestDuplicate()  {
        // setup
        val requestId = 123456;
        val zoneDateTime = ZonedDateTime.now();
        val appRequest = RequestHelper.createAppRequest(requestId, zoneDateTime);

        // when
        when(requestRepository.requestIdAndCreatedIdExists(appRequest)).thenReturn(true);
        requestService.saveRequest(appRequest);

        // then
        verify(requestRepository).requestIdAndCreatedIdExists(appRequest);
        verify(requestRepository, times(0)).save(appRequest);
    }

    @Test
    public void testCountRequest()  {
        // setup
        val zoneDateTime = ZonedDateTime.now();
        val createdId = RequestHelper.generateCreatedId(zoneDateTime);

        // when
        when(requestRepository.findByCreatedId(createdId)).thenReturn(List.of());
        requestService.countRequests(zoneDateTime);

        // then
        verify(requestRepository).findByCreatedId(createdId);
    }
}

