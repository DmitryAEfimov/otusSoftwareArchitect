package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CredentialsRepository extends CrudRepository<Credentials, UUID> {
	Optional<Credentials> findTopByLogin(String login);
}
