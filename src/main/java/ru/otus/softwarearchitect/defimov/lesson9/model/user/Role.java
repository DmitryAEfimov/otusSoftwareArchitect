package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.otus.softwarearchitect.defimov.lesson9.model.dictionary.DictionaryItem;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "ROLES")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONE, region = "app.dictionaries")
@Access(AccessType.FIELD)
public class Role extends DictionaryItem {
}
