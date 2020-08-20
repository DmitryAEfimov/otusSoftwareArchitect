package ru.otus.softwarearchitect.defimov.lesson9.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
	Optional<User> findTopByCredentials(Credentials credentials);

	Optional<User> findTopByProfileEmail(String credentials);
}
