package app.domain.tracker;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class RequestHelper {
    private RequestHelper() {
    }

    public static String generateCreatedId(final ZonedDateTime created) {
        return created.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT));
    }

    public static AppRequest createAppRequest(final Integer requestId, final ZonedDateTime zonedDateTime) {
        return new AppRequest(
                requestId,
                zonedDateTime,
                generateCreatedId(zonedDateTime)
        );
    }
}
