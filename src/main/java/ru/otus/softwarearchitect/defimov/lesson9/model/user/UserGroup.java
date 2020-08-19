package ru.otus.softwarearchitect.defimov.lesson9.model.user;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import ru.otus.softwarearchitect.defimov.lesson9.model.dictionary.DictionaryItem;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import java.util.Set;

@Entity
@DiscriminatorValue("USER_GROUPS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONE, region = "app.dictionaries")
@Access(AccessType.FIELD)
@SecondaryTable(name = "USER_GROUPS", pkJoinColumns = @PrimaryKeyJoinColumn(name = "GROUP_ID"))
public class UserGroup extends DictionaryItem {
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	@OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
	Set<Role> roles;

	public void addRole(Role role) {
		roles.add(role);
	}

	public void removeRole(Role role) {
		roles.remove(role);
	}
}
