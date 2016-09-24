package com.es.codinghub.api.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

import com.es.codinghub.api.facade.OnlineJudge;

@Entity
@Table(name="accounts")
public class Account implements JSONString {

	@Id
	@GeneratedValue
	private long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	private OnlineJudge judge;

	@NotNull
	@Size(min=1)
	private String username;

	public Account() {}

	public Account(OnlineJudge judge, String username) {
		this.judge = judge;
		this.username = username;
	}

	public long getId() {
		return id;
	}

	public OnlineJudge getJudge() {
		return judge;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Account == false)
			return false;
		Account other = (Account) obj;
		return	judge.equals(other.judge) &&
				username.equals(other.username);
	}

	@Override
	public int hashCode() {
		int hash = 0, prime = 31;
		hash = hash * prime + judge.hashCode();
		hash = hash * prime + username.hashCode();
		return hash;
	}

	@Override
	public String toJSONString() {
		JSONWriter stringer = new JSONStringer()
			.object()
				.key("id").value(id)
				.key("judge").value(judge)
				.key("username").value(username)
			.endObject();

		return stringer.toString();
	}
}
