package app.messaging.tracker;

import app.domain.tracker.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.val;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrackerJob {
    private final RequestService requestService;

    private final JmsTemplate jmsTemplate;

    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void reportTrackedRequests() {
        val zoneDateTime = ZonedDateTime.now().minusMinutes(1);
        val countRequests = requestService.countRequests(zoneDateTime);

        jmsTemplate.convertAndSend(
                app.jms.JmsDestinationDictionary.TOPIC_SYSTEM_APP_LOG,
                String.format(
                        "%s: %s unique requests received.",
                        zoneDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)),
                        countRequests
                )
        );
    }
}
