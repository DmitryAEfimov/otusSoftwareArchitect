package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class UserRepository {
	private EntityManager em;

	public UserRepository(EntityManager em) {
		this.em = em;
	}

	public Optional<User> findByEmail(String email) {
		Map<String, Object> parameters = ImmutableMap.of("email", email);
		return doQuery("User.findByEmail", parameters).findFirst();
	}

	public Optional<User> findByCredentials(Credentials credentials) {
		Map<String, Object> parameters = ImmutableMap.of("login", credentials.login, "password", credentials.password);
		return doQuery("User.findByCredentials", parameters).findFirst();
	}

	public Stream<User> findAll() {
		return doQuery("User.findAll", Collections.emptyMap());
	}

	public Optional<User> findById(UUID userId) {
		Map<String, Object> parameters = ImmutableMap.of("userId", userId);
		return doQuery("User.findById", parameters).findFirst();
	}

	public void delete(User user) {
		em.remove(user);
	}

	public User save(User user) {
		if (user.getId() == null) {
			em.persist(user);
		} else {
			em.merge(user);
		}

		return user;
	}

	private Stream<User> doQuery(String queryName, Map<String, Object> parameters) {
		TypedQuery<User> query = em.createNamedQuery(queryName, User.class);

		parameters.forEach(query::setParameter);

		return query.getResultStream();
	}
}
