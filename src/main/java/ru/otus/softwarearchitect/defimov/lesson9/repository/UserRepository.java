package ru.otus.softwarearchitect.defimov.lesson9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.lesson9.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
