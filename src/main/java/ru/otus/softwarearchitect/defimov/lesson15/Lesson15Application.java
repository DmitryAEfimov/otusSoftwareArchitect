package ru.otus.softwarearchitect.defimov.lesson15;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableCaching
public class Lesson15Application {

	public static void main(String[] args) {
		SpringApplication.run(Lesson15Application.class, args);
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
