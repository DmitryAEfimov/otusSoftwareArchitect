package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class SpecificationRepository {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private SpecificationCrudRepository crudRepository;

	public Specification findDevice(String vendor, String modelName) {
		return crudRepository.findTopByVendorAndModelName(vendor, modelName).orElse(null);
	}

	public Optional<Specification> findById(UUID id) {
		return crudRepository.findById(id);
	}

	public Specification save(Specification device) {
		return crudRepository.save(device);
	}

	public void delete(Specification device) {
		crudRepository.delete(device);
	}

	@Transactional // workaround - https://jira.spring.io/browse/DATAJPA-989
	public Set<SpecificationIdentity> findUnknownModels(Set<String> modelNames) {
		Set<Specification> knownDevices = em.createNamedQuery("Specification.findAll", Specification.class)
				.getResultStream()
				.collect(Collectors.toSet());

		Map<String, Set<String>> knownModels = knownDevices.stream()
				.collect(Collectors
						.toMap(Specification::getVendor, device -> Collections.singleton(device.getModelName()),
								Sets::union));

		HashMap<String, Set<String>> unknownModels = new HashMap<>();
		Set<String> modelNamesCopy = new HashSet<>(modelNames);
		knownModels.keySet().forEach(vendor -> {
			//models with known vendor
			modelNames.stream().filter(modelName -> modelName.startsWith(vendor)).forEach(modelName -> {
				String exactDeviceModel = modelName.replaceFirst(vendor, "").strip();
				if (!knownModels.get(vendor).contains(exactDeviceModel)) {
					unknownModels.compute(vendor,
							(key, value) -> {
								value = Optional.ofNullable(value).orElseGet(HashSet::new);
								value.add(exactDeviceModel);
								return value;
							});
				}
				modelNamesCopy.remove(modelName);
			});
		});

		return modelNamesCopy.stream().map(SpecificationIdentity::splitFullModelName).collect(Collectors.toSet());
	}
}
