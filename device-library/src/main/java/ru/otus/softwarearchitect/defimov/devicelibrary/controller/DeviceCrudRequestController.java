package ru.otus.softwarearchitect.defimov.devicelibrary.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.devicelibrary.controller.exception.DeviceChangeException;
import ru.otus.softwarearchitect.defimov.devicelibrary.controller.exception.DeviceNotFoundException;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.Device;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceClass;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceModel;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceRepository;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.DeviceCrudService;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.UnknownModelFinderService;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@Timed(value = "device_library",
		histogram = true)
public class DeviceCrudRequestController {
	private final DeviceRepository deviceRepository;
	private final DeviceCrudService crudService;
	private final UnknownModelFinderService modelFinderService;
	private final MessageSource messageSource;

	public DeviceCrudRequestController(DeviceRepository deviceRepository, DeviceCrudService crudService,
			UnknownModelFinderService modelFinderService,
			MessageSource messageSource) {
		this.deviceRepository = deviceRepository;
		this.crudService = crudService;
		this.messageSource = messageSource;
		this.modelFinderService = modelFinderService;
	}

	@PostMapping(value = "devices", produces = MediaType.APPLICATION_JSON_VALUE)
	public Device createDevice(@RequestBody Device device) {
		if (Objects.nonNull(device.getUuid())) {
			throw new DeviceChangeException(messageSource.getMessage("deviceAlreadyExists", null, Locale.US));
		}

		try {
			return crudService.save(device);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@PutMapping(value = "devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Device updateDevice(@PathVariable(name = "id") UUID id, @RequestBody DeviceClass deviceClass) {
		Device entityDevice = findByUuid(id);
		entityDevice.setDeviceClass(deviceClass);

		try {
			return crudService.save(entityDevice);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "devices/{id}")
	public void deleteDevice(@PathVariable(name = "id") UUID id) {
		Device device = findByUuid(id);

		try {
			crudService.delete(device);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "devices/unknown", produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<DeviceModel> getUnknownDeviceModels() {
		return modelFinderService.findUnknownModels();
	}

	private Device findByUuid(UUID id) {
		return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(
				messageSource.getMessage("deviceNotFound", new Object[] { id }, Locale.US)));
	}
}