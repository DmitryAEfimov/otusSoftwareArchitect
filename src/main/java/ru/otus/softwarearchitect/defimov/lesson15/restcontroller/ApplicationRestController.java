package ru.otus.softwarearchitect.defimov.lesson15.restcontroller;

import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
@Timed( value = "app_request",
		histogram = true )
public class ApplicationRestController {

	private final ProductRepository productRepository;
	private final MessageSource messageSource;
	private final PageRequest page;

	public ApplicationRestController(ProductRepository productRepository,
	                                 MessageSource messageSource,
	                                 @Value( "${app.resultCount}" ) int resCnt) {
		this.productRepository = productRepository;
		this.messageSource = messageSource;
		page = PageRequest.of(0, resCnt);
	}

	@PostMapping( value = "products", produces = MediaType.APPLICATION_JSON_VALUE )
	public Product createProduct(@RequestBody Product product) {
		Optional<Product> persistProduct = productRepository.findByVendorCodeIgnoreCase(product.getVendorCode());
		if (persistProduct.isPresent()) {
			throw new ProductChangeException(
					messageSource
							.getMessage("productAlreadyExists", new Object[] { persistProduct.get().getVendorCode() },
									Locale.US));
		}

		try {
			return productRepository.save(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@Cacheable( value = "product", key = "#vendorCode.toLowerCase()" )
	@GetMapping( value = "products/{code}", produces = MediaType.APPLICATION_JSON_VALUE )
	public Product searchProduct(@PathVariable( name = "code" ) String vendorCode) {
		return productRepository.findByVendorCodeIgnoreCase(vendorCode).orElseThrow(
				() -> new ProductNotFoundException(messageSource.getMessage("productNotFound", null, Locale.US)));
	}

	@CacheEvict( value = "product", key = "#vendorCode.toLowerCase()" )
	@DeleteMapping( value = "products/{code}" )
	public void deleteProduct(@PathVariable( name = "code" ) String vendorCode) {
		Product product = productRepository.findByVendorCodeIgnoreCase(vendorCode).orElseThrow(
				() -> new ProductNotFoundException(messageSource.getMessage("productNotFound", null, Locale.US)));

		try {
			productRepository.delete(product);
		} catch (DataIntegrityViolationException ex) {
			throw new ProductChangeException(Optional.ofNullable(ex.getRootCause()).orElse(ex).getMessage());
		}
	}

	@GetMapping( value = "products/search", produces = MediaType.APPLICATION_JSON_VALUE )
	public List<Product> searchProduct(
			@RequestParam( name = "name", required = false ) String productName,
			@RequestParam( name = "desc", required = false ) String productDesc) {
		return productRepository.findByNameOrDescription(Objects.requireNonNullElse(productName, ".*"),
				Objects.requireNonNullElse(productDesc, ".*"), page)
		                        .sorted(Comparator.comparing(Product::getName)).collect(
						Collectors.toList());
	}

	@GetMapping( value = "products", produces = MediaType.APPLICATION_JSON_VALUE )
	public List<Product> searchProduct() {
		return productRepository.findAll(page).toList();
	}
}
