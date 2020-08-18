package ru.otus.softwarearchitect.defimov.inventory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.DeviceDescriptor;

import java.util.Optional;

@Component
public class DeviceLibraryService {
	@Value(value = "${app.ext-service.dev-lib.host}")
	private String libraryServiceHostname;

	@Value(value = "${app.ext-service.dev-lib.port}")
	private int libraryServicePort;

	private final RestTemplate restTemplate;

	public DeviceLibraryService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Optional<DeviceDescriptor> findDeviceDescriptor(String deviceModelName) {
		String completeUrl = String
				.format("http://%s:%d/device_library/specs?vendor=%s&model=%s", libraryServiceHostname,
						libraryServicePort,
						DeviceDescriptor.extractVendor(deviceModelName),
						DeviceDescriptor.extractModel(deviceModelName));
		DeviceDescriptor deviceDescriptor = restTemplate.getForObject(completeUrl, DeviceDescriptor.class);
		return Optional.ofNullable(deviceDescriptor);
	}
}
