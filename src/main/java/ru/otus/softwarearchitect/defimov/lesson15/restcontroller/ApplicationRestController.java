package ru.otus.softwarearchitect.defimov.lesson15.restcontroller;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.softwarearchitect.defimov.lesson15.model.Product;
import ru.otus.softwarearchitect.defimov.lesson15.repository.ProductRepository;
import ru.otus.softwarearchitect.defimov.lesson15.restcontroller.exception.ProductChangeException;
import ru.otus.softwarearchitect.defimov.lesson15.restcontroller.exception.ProductNotFoundException;
import ru.otus.softwarearchitect.defimov.lesson15.restcontroller.exception.UnknownPropertyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@RestController
@Timed(value = "app_request",
		histogram = true)
public class ApplicationRestController {
	private static final String NAME_SEARCH_KEY = "name";
	private static final String DESCRIPTION_SEARCH_KEY = "desc";

	private final ProductRepository productRepository;
	private final MessageSource messageSource;
	private final int resCnt;

	public ApplicationRestController(ProductRepository productRepository, MessageSource messageSource, @Value("${app.resultCount}") int resCnt) {
		this.productRepository = productRepository;
		this.messageSource = messageSource;
		this.resCnt = resCnt;
	}

	@PostMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product createProduct(@RequestBody Product product) {
		if (Objects.nonNull(product.getId())) {
			throw new ProductChangeException(messageSource.getMessage("productIdAlreadyExists", null, Locale.US));
		}

		try {
			return productRepository.save(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "products/{id}")
	public void deleteProduct(@PathVariable(name = "id") UUID id) {
		Product product = findById(id);

		try {
			productRepository.delete(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "products/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> searchProduct(@RequestParam(name = "query") String query) {
		String[] keyValuePairs = query.toLowerCase().split("&");
		List<Product> products = new ArrayList<>();
		for (String keyValue : keyValuePairs) {
			PageRequest request = PageRequest.of(0, resCnt - products.size());
			if (keyValue.startsWith(NAME_SEARCH_KEY)) {
				products.addAll(productRepository.findByName(extractValue(keyValue), request));
			} else if (keyValue.startsWith(DESCRIPTION_SEARCH_KEY)) {
				products.addAll(productRepository.findByDescription(extractValue(keyValue), request));
			} else {
				throw new UnknownPropertyException(messageSource.getMessage("invalidProperty", new Object[]{extractKey(keyValue)}, Locale.US));
			}

			if (products.size() >= resCnt) {
				break;
			}
		}

		return products;
	}

	private Product findById(UUID id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(messageSource.getMessage("productNotFound", new Object[]{"id", id}, Locale.US)));
	}

	private String extractKey(String keyValue) {
		checkArgument(keyValue.contains("="));
		return keyValue.split("=")[0];
	}

	private String extractValue(String keyValue) {
		checkArgument(keyValue.contains("="));
		return keyValue.split("=")[1];
	}
}
