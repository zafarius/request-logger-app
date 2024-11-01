package app.repository.tracker;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import app.domain.tracker.AppRequest;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface RequestMapper {
    @Mapping(target = "id", ignore = true)
    RequestEntity map(AppRequest appRequest);

    AppRequest map(RequestEntity appRequest);
}
