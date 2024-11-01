package app.repository.tracker;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RequestRepositoryJpa extends CrudRepository<RequestEntity, String> {

    List<RequestEntity> findByCreatedIdEquals(String createdId);

    boolean existsByRequestIdAndCreatedId(Integer requestId, String createdId);

}
