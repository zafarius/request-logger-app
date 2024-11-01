package app.webservice.tracker;

import app.security.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackageClasses = {
        RequestControllerConfiguration.class,
        SecurityConfiguration.class
})
public class RequestControllerConfiguration {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
