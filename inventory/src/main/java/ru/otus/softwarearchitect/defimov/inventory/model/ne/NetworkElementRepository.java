package ru.otus.softwarearchitect.defimov.inventory.model.ne;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NetworkElementRepository extends CrudRepository<NetworkElement, UUID> {
	Optional<NetworkElement> findBySnmpAgentNameAndIp(String snmpAgentName, String ip);
}
