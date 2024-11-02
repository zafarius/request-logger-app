package app.jms;

public final class JmsDestinationDictionary {

  private JmsDestinationDictionary() {
  }

  public static final String TOPIC_SYSTEM_APP_LOG = "app.count";
  public static final String QUEUE_SYSTEM_APP_LOG_WRITE_NAME = "app";
  public static final String QUEUE_SYSTEM_APP_LOG_WRITE =
          TOPIC_SYSTEM_APP_LOG + "::" + QUEUE_SYSTEM_APP_LOG_WRITE_NAME;
}
