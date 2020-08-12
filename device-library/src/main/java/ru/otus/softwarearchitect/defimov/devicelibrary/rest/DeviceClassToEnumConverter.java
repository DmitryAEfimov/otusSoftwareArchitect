package ru.otus.softwarearchitect.defimov.devicelibrary.rest;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import ru.otus.softwarearchitect.defimov.devicelibrary.model.DeviceClass;

import java.util.Arrays;

public class DeviceClassToEnumConverter implements Converter<String, DeviceClass> {
	@Override
	public DeviceClass convert(@NonNull String source) {
		return Arrays.stream(DeviceClass.values()).filter(value -> value.name().equalsIgnoreCase(source)).findFirst()
				.orElseThrow();
	}
}