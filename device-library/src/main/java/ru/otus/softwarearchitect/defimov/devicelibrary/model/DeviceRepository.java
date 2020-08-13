package ru.otus.softwarearchitect.defimov.devicelibrary.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceRepository extends CrudRepository<Device, UUID>, UnknownDeviceRepository {
}
