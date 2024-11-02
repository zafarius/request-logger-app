
package app.messaging.tracker;

import jakarta.jms.JMSException;
import org.apache.activemq.artemis.jms.client.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountReceiverTest {

    @InjectMocks
    private CountReceiver countReceiver;

    @Mock
    private ActiveMQTextMessage activeMQTextMessage;


    @Test
    public void testReceiver() throws JMSException {
        // when
        when(activeMQTextMessage.getText()).thenReturn("");
        countReceiver.receive(activeMQTextMessage);

       // then
       verify(activeMQTextMessage).getText();
    }
}

