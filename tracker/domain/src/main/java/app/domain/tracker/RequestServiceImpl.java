package app.domain.tracker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;


    @Override
    public void saveRequest(final AppRequest appRequest) {
        if (!requestRepository.requestIdAndCreatedIdExists(appRequest)) {
            requestRepository.save(appRequest);
        }
    }

    @Override
    public Integer countRequests(final ZonedDateTime zonedDateTime) {
        return requestRepository
                .findByCreatedId(RequestHelper.generateCreatedId(zonedDateTime))
                .size();
    }
}
