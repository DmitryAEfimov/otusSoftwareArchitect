package ru.otus.softwarearchitect.defimov.auth.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.softwarearchitect.defimov.auth.model.User;


public interface UserRepository extends MongoRepository<User, String> {
}
