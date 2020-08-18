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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.devicelibrary.controller.exception.DeviceChangeException;
import ru.otus.softwarearchitect.defimov.devicelibrary.controller.exception.ExternalSystemException;
import ru.otus.softwarearchitect.defimov.devicelibrary.controller.exception.SpecificationNotFoundException;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceClass;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.Specification;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.SpecificationIdentity;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.SpecificationService;
import ru.otus.softwarearchitect.defimov.devicelibrary.service.exception.DiscoveryReportNotExistsExcception;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@Timed(value = "device_library",
		histogram = true)
public class SpecificationRequestController {
	private final SpecificationService crudService;
	private final MessageSource messageSource;

	public SpecificationRequestController(SpecificationService crudService, MessageSource messageSource) {
		this.crudService = crudService;
		this.messageSource = messageSource;
	}

	@GetMapping(value = "specs", produces = MediaType.APPLICATION_JSON_VALUE)
	public Specification getSpecification(@RequestParam(name = "vendor") String vendorName,
			@RequestParam(name = "model") String modelName) {
		try {
			return crudService.findSpecification(vendorName, modelName);
		} catch (DiscoveryReportNotExistsExcception ex) {
			throw new ExternalSystemException(ex);
		}
	}

	@GetMapping(value = "specs/unknown", produces = MediaType.APPLICATION_JSON_VALUE)
	public Set<SpecificationIdentity> getUnknownDeviceModels() throws ExternalSystemException {
		try {
			return crudService.findUnknownModels();
		} catch (DiscoveryReportNotExistsExcception ex) {
			throw new ExternalSystemException(ex);
		}
	}

	@PostMapping(value = "specs", produces = MediaType.APPLICATION_JSON_VALUE)
	public Specification createDevice(@RequestBody Specification device) {
		if (Objects.nonNull(device.getUuid())) {
			throw new DeviceChangeException(messageSource.getMessage("deviceAlreadyExists", null, Locale.US));
		}

		try {
			return crudService.save(device);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		} catch (DiscoveryReportNotExistsExcception ex) {
			throw new ExternalSystemException(ex);
		}
	}

	@PutMapping(value = "specs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Specification updateDevice(@PathVariable(name = "id") UUID id, @RequestBody DeviceClass deviceClass) {
		Specification entityDevice = findByUuid(id);
		entityDevice.setDeviceClass(deviceClass);

		try {
			return crudService.save(entityDevice);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		} catch (DiscoveryReportNotExistsExcception ex) {
			throw new ExternalSystemException(ex);
		}
	}

	@DeleteMapping(value = "specs/{id}")
	public void deleteSpecification(@PathVariable(name = "id") UUID id) {
		Specification specification = findByUuid(id);

		try {
			crudService.delete(specification);
		} catch (DataIntegrityViolationException ex) {
			throw new DeviceChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		} catch (DiscoveryReportNotExistsExcception ex) {
			throw new ExternalSystemException(ex);
		}
	}

	private Specification findByUuid(UUID id) {
		return crudService.findSpecificationById(id).orElseThrow(() -> new SpecificationNotFoundException(
				messageSource.getMessage("specificationNotFound", new Object[] { id }, Locale.US)));
	}
}