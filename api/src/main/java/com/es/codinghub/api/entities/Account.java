package com.es.codinghub.api.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

@Entity
@Table(name="accounts")
public class Account implements JSONString {

	@Id
	@GeneratedValue
	private long id;

	private String judge;
	private String username;

	public Account() {}

	public Account(String judge, String username) {
		this.judge = judge;
		this.username = username;
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
