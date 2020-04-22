package ru.otus.softwarearchitect.defimov.lesson5.probe;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProbeRestController {
	@GetMapping(value = "health", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> checkHealth() {
		return ImmutableMap.of("status", HttpStatus.OK.getReasonPhrase());
	}
}
