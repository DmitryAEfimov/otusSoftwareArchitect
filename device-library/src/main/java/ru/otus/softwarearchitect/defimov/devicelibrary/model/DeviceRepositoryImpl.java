package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DeviceRepositoryImpl implements UnknownDeviceRepository {

	@PersistenceContext
	private EntityManager em;

	public Set<Device> findUnknown(Set<Device> devices) {
		Set<Device> knownDevices = em.createNamedQuery("findAll", Device.class).getResultStream()
				.collect(Collectors.toSet());

		return Sets.difference(devices, knownDevices);
	}
}
