package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NetworkElementRepository extends CrudRepository<NetworkElement, NetworkElementId> {
	Optional<NetworkElement> findById(Example<NetworkElement> id);
}
