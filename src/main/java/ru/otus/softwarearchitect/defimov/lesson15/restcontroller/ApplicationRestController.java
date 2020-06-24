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

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Timed(value = "app_request",
		histogram = true)
public class ApplicationRestController {

	private final ProductRepository productRepository;
	private final MessageSource messageSource;
	private final PageRequest page;


	public ApplicationRestController(ProductRepository productRepository, MessageSource messageSource,
									 @Value("${app.resultCount}") int resCnt) {
		this.productRepository = productRepository;
		this.messageSource = messageSource;
		page = PageRequest.of(0, resCnt);
	}

	@PostMapping(value = "products", produces = MediaType.APPLICATION_JSON_VALUE)
	public Product createProduct(@RequestBody Product product) {
		Product persistProduct = productRepository.findByVendorCode(product.getVendorCode());
		if (persistProduct != null) {
			throw new ProductChangeException(
					messageSource.getMessage("productAlreadyExists", new Object[]{product.getVendorCode()}, Locale.US));
		}

		try {
			return productRepository.save(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@DeleteMapping(value = "products/{id}")
	public void deleteProduct(@PathVariable(name = "id") String id) {
		Product product = productRepository.findById(id).orElseThrow(
				() -> new ProductNotFoundException(messageSource.getMessage("productNotFound", null, Locale.US)));

		try {
			productRepository.delete(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping(value = "products/search", params = {"!code"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> searchProduct(@RequestParam(name = "name", required = false) String productName,
									   @RequestParam(name = "desc", required = false) String productDesc) {
		return productRepository.findByNameOrDescription(Objects.requireNonNullElse(productName, ".*"),
				Objects.requireNonNullElse(productDesc, ".*"), page)
								.sorted(Comparator.comparing(Product::getName)).collect(Collectors.toList());
	}

	@GetMapping(value = "products/search", params = {"code"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> searchProduct(@RequestParam(name = "code") String vendorCode) {
		return productRepository.findByVendorCodeRegex(vendorCode, page).sorted(Comparator.comparing(Product::getName))
								.collect(Collectors.toList());
	}

	@GetMapping(value = "products/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Product> searchProduct() {
		return productRepository.findAll(page).toList();
	}
}
