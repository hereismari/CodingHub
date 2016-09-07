package com.es.codinghub.api.entities;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class Problem implements JSONString {

	private String id;
	private String name;
	private int solvedCount;

	public Problem(String id, String name, int solvedCount) {
		this.id = id;
		this.name = name;
		this.solvedCount = solvedCount;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getSolvedCount() {
		return solvedCount;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Problem == false)
			return false;
		Problem other = (Problem) obj;
		return id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toJSONString() {
		JSONWriter stringer = new JSONStringer()
			.object()
				.key("id").value(id)
				.key("name").value(name)
				.key("solvedCount").value(solvedCount)
			.endObject();

		return stringer.toString();
	}

}
