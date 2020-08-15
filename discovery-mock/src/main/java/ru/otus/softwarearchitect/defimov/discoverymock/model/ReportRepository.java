package ru.otus.softwarearchitect.defimov.discoverymock.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.UUID;

public interface ReportRepository extends MongoRepository<DiscoveryReportItem, String> {
	Page<DiscoveryReportItem> findAllByReportId(UUID reportId, Pageable pageable);
	@Query(value = "{'reportId': ?0}", count = true)
	int countItemsByReportId(UUID reportId);
}
