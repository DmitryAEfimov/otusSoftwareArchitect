package ru.otus.softwarearchitect.defimov.devicelibrary.rest;

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
import ru.otus.softwarearchitect.defimov.devicelibrary.model.Device;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceClass;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceRepository;
import ru.otus.softwarearchitect.defimov.devicelibrary.rest.exception.DeviceChangeException;
import ru.otus.softwarearchitect.defimov.devicelibrary.rest.exception.DeviceNotFoundException;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@Timed(value = "device_library",
		histogram = true)
public class RequestController {
	private final DeviceRepository deviceRepository;
	private final MessageSource messageSource;

	public RequestController(DeviceRepository deviceRepository, MessageSource messageSource) {
		this.deviceRepository = deviceRepository;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "devices", produces = MediaType.APPLICATION_JSON_VALUE)
	public Device createDevice(@RequestBody Device device) {
		if (Objects.nonNull(device.getUuid())) {
			throw new DeviceChangeException(messageSource.getMessage("userIdAlreadyExists", null, Locale.US));
		}

		try {
			return deviceRepository.save(device);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@PutMapping(value = "devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Device updateDevice(@PathVariable(name = "id") UUID id, @RequestBody DeviceClass deviceClass) {
		Device entityDevice = findByUuid(id);
		entityDevice.setDeviceClass(deviceClass);

		try {
			return deviceRepository.save(entityDevice);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "devices/{id}")
	public void deleteUser(@PathVariable(name = "id") UUID id) {
		Device user = findByUuid(id);

		try {
			deviceRepository.delete(user);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "devices", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Device> findAll() {
		return deviceRepository.findAll();
	}

	private Device findByUuid(UUID id) {
		return deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(
				messageSource.getMessage("userNotFound", new Object[] { id }, Locale.US)));
	}
}