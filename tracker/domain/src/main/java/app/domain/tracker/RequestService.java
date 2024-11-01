package app.domain.tracker;

import java.time.ZonedDateTime;

public interface RequestService {
    void saveRequest(AppRequest appRequest);

    Integer countRequests(ZonedDateTime zonedDateTime);
}
