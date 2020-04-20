package ru.otus.softwarearchitect.defimov.lesson2.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {
	private final ObjectMapper mapper;

	public AppRestController(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@GetMapping(value = "health")
	public ObjectNode checkHealth() {
		ObjectNode response = mapper.createObjectNode();
		response.put("status", HttpStatus.OK.getReasonPhrase());

		return response;
	}
}
