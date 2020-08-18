package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.Specification;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.SpecificationIdentity;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.SpecificationRepository;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.exception.DiscoveryReportNotExistsExcception;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class SpecificationService {
	private final SpecificationRepository specificationRepository;
	private final CacheService cacheService;

	public SpecificationService(SpecificationRepository specificationRepository,
			CacheService cacheService) {
		this.specificationRepository = specificationRepository;
		this.cacheService = cacheService;
	}

	public Specification findSpecification(String vendorName, String modelName) throws DiscoveryReportNotExistsExcception {
		return specificationRepository.findDevice(vendorName, modelName);
	}

	public Set<SpecificationIdentity> findUnknownModels() throws DiscoveryReportNotExistsExcception {
		return cacheService.getUnknownModels();
	}

	public Optional<Specification> findSpecificationById(UUID specId) {
		return specificationRepository.findById(specId);
	}

	public Specification save(Specification specification) throws DiscoveryReportNotExistsExcception {
		Specification entityDevice = specificationRepository.save(specification);
		cacheService.updateCache();

		return entityDevice;
	}

	public void delete(Specification specification) throws DiscoveryReportNotExistsExcception {
		specificationRepository.delete(specification);
		cacheService.updateCache();
	}
}
