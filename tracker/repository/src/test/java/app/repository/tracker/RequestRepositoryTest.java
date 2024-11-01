
package app.repository.tracker;

import app.domain.tracker.RequestHelper;
import app.domain.tracker.RequestRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import lombok.val;

import java.time.ZonedDateTime;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;


@EnableAutoConfiguration
@DataJpaTest
@ContextConfiguration(classes = RepositoryConfiguration.class)
@TestPropertySource(
        properties = {
                "files.output_dir=build",
                "spring.jpa.hibernate.ddl-auto=validate",
                "spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl",
                "spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
                "spring.jpa.properties.hibernate.format_sql=true",
                "spring.jpa.properties.hibernate.show_sql=true",
                "spring.jpa.properties.hibernate.globally_quoted_identifiers=true"
        })
public class RequestRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    @Test
    public void testSaveAndGetRequests()  {
        // setup
        val created = ZonedDateTime.now();
        val appRequest1 = RequestHelper.createAppRequest(1, created);
        val appRequest2 = RequestHelper.createAppRequest(2, created);


        // when
        requestRepository.save(appRequest1);
        requestRepository.save(appRequest2);
        entityManager.flush();

        // then
        val result = requestRepository.findByCreatedId(RequestHelper.generateCreatedId(created));
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getRequestId()).isEqualTo(appRequest1.getRequestId());
        assertThat(result.get(0).getCreated()).isEqualTo(appRequest1.getCreated());
        assertThat(result.get(1).getRequestId()).isEqualTo(appRequest2.getRequestId());
        assertThat(result.get(1).getCreated()).isEqualTo(appRequest2.getCreated());
    }

    @Test
    public void testSaveRequestDuplicateFails()  {
        // setup
        val created = ZonedDateTime.now();
        val requestId = 1;
        val appRequest1 = RequestHelper.createAppRequest(requestId, created);
        val appRequest2 = RequestHelper.createAppRequest(requestId, created);


        // when
        requestRepository.save(appRequest1);
        requestRepository.save(appRequest2);

        // then
        assertThatThrownBy(() -> {
            entityManager.flush();
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void testCheckRequestIdAndCreatedIdExists()  {
        // setup
        val created = ZonedDateTime.now();
        val appRequest1 = RequestHelper.createAppRequest(1, created);
        val appRequest2 = RequestHelper.createAppRequest(2, created);

        // when
        requestRepository.save(appRequest1);
        entityManager.flush();

        // then
        val exists = requestRepository.requestIdAndCreatedIdExists(appRequest1);
        val notExists = requestRepository.requestIdAndCreatedIdExists(appRequest2);

        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();

    }
}
