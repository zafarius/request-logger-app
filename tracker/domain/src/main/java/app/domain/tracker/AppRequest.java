package app.domain.tracker;

import lombok.Data;
import java.time.ZonedDateTime;

@Data
public class AppRequest {
    private final Integer requestId;
    private final ZonedDateTime created;
    private final String createdId;
}
