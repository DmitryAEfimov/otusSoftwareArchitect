package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UnknownDeviceRepository {
	Set<Device> findUnknown(Set<Device> devices);
}
