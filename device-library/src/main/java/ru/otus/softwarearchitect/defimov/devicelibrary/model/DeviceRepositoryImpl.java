package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DeviceRepositoryImpl {

	private final DeviceRepository deviceRepository;

	@Autowired
	public DeviceRepositoryImpl(DeviceRepository deviceRepository) {
		this.deviceRepository = deviceRepository;
	}

	public Set<Device> findUnknown(Set<Device> devices) {
		Set<Device> knownDevices = Set.copyOf(deviceRepository.findAll());

		return Sets.difference(devices, knownDevices);
	}
}
