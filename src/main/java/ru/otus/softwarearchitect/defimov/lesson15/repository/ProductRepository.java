package ru.otus.softwarearchitect.defimov.lesson15.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.softwarearchitect.defimov.lesson15.model.Product;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
	@Query("{ $or: [ {name : {$regex: ?0, $options: 'i' }, description : {$regex: ?1, $options: 'i' }} ] }")
	Stream<Product> findByNameOrDescription(String productName, String description, Pageable pageable);

	Optional<Product> findByVendorCodeIgnoreCase(String vendorCode);
}
