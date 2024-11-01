package app.webservice.tracker;

import app.domain.tracker.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.val;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrackerJob {
    private final RequestService requestService;


    @Scheduled(cron = "0 * * * * *")
    public void reportTrackedRequests() {
        val zoneDateTime = ZonedDateTime.now().minusMinutes(1);
        val count = requestService.countRequests(zoneDateTime);

        log.info(
                "At {}: {} requests received.",
                zoneDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                count
        );
    }
}
