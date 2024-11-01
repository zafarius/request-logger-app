package app.webservice.tracker;

import app.security.SecurityConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        RequestControllerConfiguration.class,
        SecurityConfiguration.class
})
public class RequestControllerConfiguration {
}
