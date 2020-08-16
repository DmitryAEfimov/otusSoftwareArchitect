package ru.otus.softwarearchitect.defimov.discoverymock.service.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class DiscoveryReportConverter implements MessageConverter {
	@Value(value = "${app.service.identity}")
	private String serviceIdentity;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override public Message toMessage(Object object, MessageProperties messageProperties)
			throws MessageConversionException {
		byte[] body;
		try {
			body = objectMapper.writeValueAsBytes(object);
		} catch (JsonProcessingException e) {
			throw new MessageConversionException(e.getCause().getMessage());
		}

		messageProperties.setContentEncoding(StandardCharsets.UTF_8.name());
		messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
		messageProperties.setCorrelationId(UUID.randomUUID().toString());
		messageProperties.setContentLength(body.length);
		messageProperties.getHeaders().put("serviceName", serviceIdentity);

		return MessageBuilder.withBody(body).andProperties(messageProperties)
				.build();
	}

	@Override public Object fromMessage(Message message) throws MessageConversionException {
		try {
			return objectMapper.readValue(message.getBody(), DiscoveryReportDto.class);
		} catch (IOException e) {
			throw new MessageConversionException(e.getCause().getMessage());
		}
	}
}
