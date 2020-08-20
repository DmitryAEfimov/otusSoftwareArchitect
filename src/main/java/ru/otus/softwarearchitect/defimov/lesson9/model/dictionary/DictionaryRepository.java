package ru.otus.softwarearchitect.defimov.lesson9.model.dictionary;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DictionaryRepository extends CrudRepository<Dictionary, UUID> {
	Dictionary findTopByDictionaryType(DictionaryType type);
}
