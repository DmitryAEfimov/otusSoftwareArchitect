package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.google.common.collect.Sets;
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
import java.util.stream.Collectors;

@Repository
public class UnknownDeviceRepository {

	@PersistenceContext
	private EntityManager em;

	@Transactional // workaround - https://jira.spring.io/browse/DATAJPA-989
	public Map<String, Set<String>> findUnknownModels(Set<String> modelNames) {
		Set<Device> knownDevices = em.createNamedQuery("Device.findAll", Device.class).getResultStream()
				.collect(Collectors.toSet());

		Map<String, Set<String>> knownModels = knownDevices.stream()
				.collect(Collectors.toMap(Device::getVendor, device -> Collections.singleton(device.getModelName()),
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

		unknownModels.putAll(
				modelNamesCopy.stream().map(modelName -> modelName.split("\\s", 2))
						.collect(
								Collectors.groupingBy(array -> array[0],
										Collectors.mapping(array -> array[1], Collectors.toSet()))
						)
		);

		return unknownModels;
	}
}
