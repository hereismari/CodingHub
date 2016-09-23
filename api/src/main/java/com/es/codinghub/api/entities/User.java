package com.es.codinghub.api.entities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

import com.es.codinghub.api.facade.OnlineJudge;

@Entity
@Table(name="users")
public class User implements JSONString {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Column(unique=true)
	@Pattern(regexp=".+@.+\\..+")
	private String email;

	@NotNull
	@Size(min=6)
	private String password;

	@OneToMany(orphanRemoval=true, cascade=CascadeType.ALL)
	@JoinColumn(name="userid")
	private List<Account> accounts = new ArrayList<>();

	public User() {}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public void addAccount(OnlineJudge judge, String username) {
		Account account = new Account(judge, username);
		if (accounts.contains(account) == false) accounts.add(account);
		else throw new IllegalArgumentException();
	}

	public void removeAccount(final long accountid) {
		accounts.removeIf(new Predicate<Account>() {
			@Override
			public boolean test(Account account) {
				return account.getId() == accountid;
			}
		});
	}

	public long getId() {
		return id;
	}

	public List<Account> getAccounts() {
		return accounts;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User == false)
			return false;
		User other = (User) obj;
		return	email.equals(other.email);
	}

	@Override
	public int hashCode() {
		return email.hashCode();
	}

	@Override
	public String toJSONString() {
		JSONWriter stringer = new JSONStringer()
			.object()
				.key("id").value(id)
				.key("email").value(email)
				.key("accounts").value(accounts)
			.endObject();

		return stringer.toString();
	}
}
