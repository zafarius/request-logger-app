package app.messaging.tracker;

import app.jms.JmsDestinationDictionary;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CountReceiver {
    @Transactional
    @JmsListener(destination = JmsDestinationDictionary.QUEUE_SYSTEM_APP_LOG_WRITE)
    public void receive(final TextMessage textMessage) throws RuntimeException, JMSException {
       log.info(textMessage.getText());
    }
}
