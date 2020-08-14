package ru.otus.softwarearchitect.defimov.inventory.model.location;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends CrudRepository<Location, UUID> {
	Optional<Location> findOne(Example<Location> location);
}
