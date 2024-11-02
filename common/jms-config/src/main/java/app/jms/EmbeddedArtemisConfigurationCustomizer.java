package app.jms;

import java.util.Optional;
import java.util.Set;

import lombok.val;
import org.apache.activemq.artemis.api.core.QueueConfiguration;
import org.apache.activemq.artemis.api.core.RoutingType;
import org.apache.activemq.artemis.core.config.CoreAddressConfiguration;
import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisConfigurationCustomizer;
import org.springframework.context.annotation.Configuration;

/*
  This class implements a logical or-condition to configure an embedded artemis broker for:
  * local startup
  * integration tests
  * end-to-end tests

  See https://www.baeldung.com/spring-conditional-annotations

  If no embedded broker is used (e.g. on k8s environments), this bean will not be loaded.

  Integration tests do not know about the `spring.artemis.mode = "EMBEDDED"` configuration but
  are loading the `embeddedActiveMq` bean.

  Using only `embeddedActiveMq` as condition does not work for end-to-end tests and
  local startup though.
*/
class EmbeddedArtemisCondition extends AnyNestedCondition {

  EmbeddedArtemisCondition() {
    super(ConfigurationPhase.REGISTER_BEAN);
  }

  @Configuration
  @ConditionalOnBean(name = "embeddedActiveMq")
  static class IntegrationTestCustomizer extends EmbeddedArtemisConfigurationCustomizer {
  }

  @Configuration
  @ConditionalOnProperty(prefix = "spring.artemis", name = "mode", havingValue = "EMBEDDED")
  static class EndToEndTestAndLocalStartupCustomizer
          extends EmbeddedArtemisConfigurationCustomizer {
  }
}

public abstract class EmbeddedArtemisConfigurationCustomizer
        implements ArtemisConfigurationCustomizer {

  /*
   ATTENTION!!!!!

   It is NOT SUFFICIENT to configure production topics and queues here,
   we also have to configure the broker.xml

   Background:
   - topics and queues are not auto-created (anymore)
   - this configuration is only used whenever an embedded artemis broker is involved
     - which translates to all sorts of tests and local development
  */
  @Override
  public void customize(final org.apache.activemq.artemis.core.config.Configuration conf) {
    val addressSettings = conf.getAddressSettings();

    addressSettings
            .get("#")
            .setAutoCreateAddresses(false)
            .setAutoCreateQueues(false)
            .setAutoDeleteAddresses(false)
            .setAutoDeleteQueues(false);

    configureQueue(
            conf,
            JmsDestinationDictionary.TOPIC_SYSTEM_APP_LOG,
            JmsDestinationDictionary.QUEUE_SYSTEM_APP_LOG_WRITE_NAME);
  }

  public static void configureQueue(
          final org.apache.activemq.artemis.core.config.Configuration configuration,
          final String addressName,
          final String queueName) {
    configureQueues(configuration, addressName, Set.of(queueName));
  }

  public static void configureFilteredQueue(
          final org.apache.activemq.artemis.core.config.Configuration configuration,
          final String addressName,
          final String queueName,
          final String filter) {
    configureFilteredQueues(configuration, addressName, Set.of(queueName), Optional.of(filter));
  }

  public static void configureQueues(
          final org.apache.activemq.artemis.core.config.Configuration configuration,
          final String addressName,
          final Set<String> queueNames) {
    configureFilteredQueues(configuration, addressName, queueNames, Optional.empty());
  }

  private static void configureFilteredQueues(
          final org.apache.activemq.artemis.core.config.Configuration configuration,
          final String addressName,
          final Set<String> queueNames,
          final Optional<String> filter) {
    val addressConf = new CoreAddressConfiguration();
    addressConf.setName(addressName);
    addressConf.addRoutingType(RoutingType.MULTICAST);

    queueNames.forEach(
            qn -> {
              val queueConf = new QueueConfiguration(qn);
              queueConf.setAddress(addressName);
              queueConf.setRoutingType(RoutingType.MULTICAST);
              filter.ifPresent(queueConf::setFilterString);
              addressConf.addQueueConfiguration(queueConf);
            });
    configuration.addAddressConfiguration(addressConf);
  }
}
