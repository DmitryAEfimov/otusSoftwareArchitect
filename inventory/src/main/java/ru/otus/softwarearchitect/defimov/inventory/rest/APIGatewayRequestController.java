package ru.otus.softwarearchitect.defimov.inventory.rest;

import io.micrometer.core.annotation.Timed;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.inventory.model.location.Location;
import ru.otus.softwarearchitect.defimov.inventory.model.location.LocationRepository;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElement;
import ru.otus.softwarearchitect.defimov.inventory.model.ne.NetworkElementRepository;
import ru.otus.softwarearchitect.defimov.inventory.rest.exception.NetworkElementChangeException;
import ru.otus.softwarearchitect.defimov.inventory.rest.exception.NetworkElementNotFoundException;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@Timed(value = "inventory",
		histogram = true)
public class APIGatewayRequestController {
	private final NetworkElementRepository neRepository;
	private final LocationRepository locationRepository;
	private final MessageSource messageSource;

	public APIGatewayRequestController(NetworkElementRepository deviceRepository, LocationRepository locationRepository,
			MessageSource messageSource) {
		this.neRepository = deviceRepository;
		this.locationRepository = locationRepository;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "network_elements", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkElement createNetworkElement(@RequestBody NetworkElement networkElement) {
		if (Objects.nonNull(networkElement.getId())) {
			throw new NetworkElementChangeException(messageSource.getMessage("neAlreadyExists", null, Locale.US));
		}

		try {
			Location entityLocation = findOrCreateLocation(networkElement.getLocation());
			networkElement.setLocation(entityLocation);
			return neRepository.save(networkElement);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(
					Optional.of(ex.getMostSpecificCause()).orElse(ex).getMessage());
		}
	}

	@PutMapping(value = "network_elements/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkElement updateNetworkElement(@PathVariable(name = "id") UUID id, @RequestBody Location location) {
		NetworkElement entityNe = findById(id);
		entityNe.setLocation(findOrCreateLocation(location));

		try {
			return neRepository.save(entityNe);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "network_elements/{id}")
	public void deleteNetworkElement(@PathVariable(name = "id") UUID id) {
		NetworkElement networkElement = findById(id);

		try {
			neRepository.delete(networkElement);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	private Location findOrCreateLocation(Location location) {
		return locationRepository.findOne(Example.of(location)).orElseGet(() -> locationRepository.save(location));
	}

	private NetworkElement findById(UUID id) {
		return neRepository.findById(id).orElseThrow(() -> new NetworkElementNotFoundException(
				messageSource.getMessage("neNotFound", new Object[] { id }, Locale.US)));
	}
}