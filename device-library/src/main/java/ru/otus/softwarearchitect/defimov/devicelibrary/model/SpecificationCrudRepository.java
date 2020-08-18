package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
interface SpecificationCrudRepository extends CrudRepository<Specification, UUID> {
	Optional<Specification> findTopByVendorAndModelName(String vendor, String modelName);
}
