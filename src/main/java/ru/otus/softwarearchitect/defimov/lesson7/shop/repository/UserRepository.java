package ru.otus.softwarearchitect.defimov.lesson7.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.lesson7.shop.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
