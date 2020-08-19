package ru.otus.softwarearchitect.defimov.lesson9.model.dictionary;

import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "DICTIONARIES")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Access(AccessType.FIELD)
@TypeDef(
		name = "pgsql_enum",
		typeClass = PostgreSQLEnumType.class
)
public class Dictionary {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private UUID id;

	@Enumerated(EnumType.STRING)
	@Column(name = "DICTIONARY_TYPE", updatable = false)
	@Type(type = "pgsql_enum")
	private DictionaryType dictionaryType;

	@OneToMany(mappedBy = "dictionary", fetch = FetchType.EAGER, cascade = { CascadeType.MERGE }, orphanRemoval = true)
	private Set<DictionaryItem> elements;

	protected Dictionary() {
		//		JPA only
	}

	public Set<DictionaryItem> getElements() {
		return Collections.unmodifiableSet(elements);
	}

	public DictionaryType getDictionaryType() {
		return dictionaryType;
	}

	void addElement(DictionaryItem item) {
		elements.add(item);
		item.setDictionary(this);
	}

	void removeElement(DictionaryItem item) {
		elements.remove(item);
		item.setDictionary(null);
	}
}
