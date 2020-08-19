package ru.otus.softwarearchitect.defimov.lesson9.model.dictionary;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "DICTIONARY_ITEMS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONE, region = "app.dictionaries")
@Access(AccessType.FIELD)
@DiscriminatorFormula("select di.dictionary_type from dictionaries d join dictionary_items di on di.dictionary_id = d.id where di.id = ID")
public abstract class DictionaryItem {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private UUID id;

	@ManyToOne(optional = false, cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "DICTIONARY_ID", updatable = false, nullable = false)
	protected Dictionary dictionary;

	@Column(name = "VALUE")
	protected String value;

	@Column(name = "IS_SYSTEM", updatable = false)
	private boolean isSystem;

	protected DictionaryItem() {
		//		JPA only
	}

	public DictionaryItem(Dictionary dictionary, String value) {
		this.dictionary = dictionary;
		this.value = value;
	}

	public Dictionary getDictionary() {
		return dictionary;
	}

	void setDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
