package app.domain.tracker;

import java.util.List;

public interface RequestRepository {
    void save(AppRequest appRequest);

    List<AppRequest> findByCreatedId(String createdId);

    boolean requestIdAndCreatedIdExists(AppRequest appRequest);
}
