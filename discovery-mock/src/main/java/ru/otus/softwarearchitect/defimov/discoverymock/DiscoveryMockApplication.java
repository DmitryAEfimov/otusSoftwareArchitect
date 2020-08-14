package ru.otus.softwarearchitect.defimov.discoverymock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@SpringBootApplication
public class DiscoveryMockApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryMockApplication.class, args);
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

		messageSource.setBasenames("classpath:errorMessages");
		messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
		messageSource.setDefaultLocale(Locale.US);

		return messageSource;
	}

}
