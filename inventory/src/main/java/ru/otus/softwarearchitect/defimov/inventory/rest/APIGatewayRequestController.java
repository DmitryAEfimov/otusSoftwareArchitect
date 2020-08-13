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
	private final MessageSource messageSource;

	public APIGatewayRequestController(NetworkElementRepository deviceRepository, MessageSource messageSource) {
		this.neRepository = deviceRepository;
		this.messageSource = messageSource;
	}

	@PostMapping(value = "network_elements", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkElement createNetworkElement(@RequestBody NetworkElement networkElement) {
		if (Objects.nonNull(networkElement.getId())) {
			throw new NetworkElementChangeException(messageSource.getMessage("neAlreadyExists", null, Locale.US));
		}

		try {
			return neRepository.save(networkElement);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@PutMapping(value = "network_elements/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public NetworkElement updateDevice(@PathVariable(name = "id") UUID id, @RequestBody Location location) {
		NetworkElement entityNe = findById(id);
		entityNe.setLocation(location);

		try {
			return neRepository.save(entityNe);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "network_elements/{id}")
	public void deleteUser(@PathVariable(name = "id") UUID id) {
		NetworkElement user = findById(id);

		try {
			neRepository.delete(user);
		} catch (DataIntegrityViolationException ex) {
			throw new NetworkElementChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	private NetworkElement findById(UUID id) {
		NetworkElement ne = new NetworkElement();
		ne.setId(id);

		return neRepository.findById(Example.of(ne)).orElseThrow(() -> new NetworkElementNotFoundException(
				messageSource.getMessage("neNotFound", new Object[] { id }, Locale.US)));
	}
}