package app.repository.tracker;

import app.domain.tracker.AppRequest;
import app.domain.tracker.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestRepositoryImpl implements RequestRepository {
    private final RequestRepositoryJpa requestRepositoryJpa;
    private final RequestMapper requestMapper;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void save(final AppRequest appRequest) {
        requestRepositoryJpa.save(requestMapper.map(appRequest));
    }

    @Override
    public List<AppRequest> findByCreatedId(final String createdId) {
        return requestRepositoryJpa.findByCreatedIdEquals(createdId)
                .stream()
                .map(requestMapper::map)
                .toList();
    }

    @Override
    public boolean requestIdAndCreatedIdExists(final AppRequest appRequest) {
        return requestRepositoryJpa.existsByRequestIdAndCreatedId(appRequest.getRequestId(), appRequest.getCreatedId());
    }
}
