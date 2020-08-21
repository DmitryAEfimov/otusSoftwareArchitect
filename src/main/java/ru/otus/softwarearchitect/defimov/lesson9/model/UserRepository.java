package ru.otus.softwarearchitect.defimov.lesson9.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findTopByCredentialsLogin(String login);

	Optional<User> findTopByProfileEmail(String credentials);
}
