package ru.otus.softwarearchitect.defimov.inventory.model.location;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends CrudRepository<Location, UUID> {
	Optional<Location> findOne(Example<Location> location);
}
