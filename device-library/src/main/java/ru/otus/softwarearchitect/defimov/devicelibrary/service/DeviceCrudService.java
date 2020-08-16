package ru.otus.softwarearchitect.defimov.devicelibrary.service;

import org.springframework.stereotype.Component;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.Device;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceRepository;

@Component
public class DeviceCrudService {
	private final DeviceRepository deviceRepository;
	private final UnknownModelFinderService finderService;

	public DeviceCrudService(DeviceRepository deviceRepository, UnknownModelFinderService finderService) {
		this.deviceRepository = deviceRepository;
		this.finderService = finderService;
	}

	public Device save(Device device) {
		Device entityDevice = deviceRepository.save(device);
		finderService.clearCache();

		return entityDevice;
	}

	public void delete(Device device) {
		deviceRepository.delete(device);
		finderService.clearCache();
	}

}
