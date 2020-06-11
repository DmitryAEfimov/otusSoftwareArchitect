package ru.otus.softwarearchitect.defimov.lesson15.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.lesson15.model.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends MongoRepository<Product, UUID> {
	@Query("{name : {$regex: ?0, $options: 'i' }}")
	List<Product> findByName(String productName, Pageable page);

	@Query("{name : {$regex: ?0, $options: 'i' }}")
	List<Product> findByDescription(String description, Pageable page);
}
