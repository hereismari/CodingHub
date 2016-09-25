package com.es.codinghub.api.entities;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

import com.es.codinghub.api.facade.OnlineJudge;

public class Problem implements JSONString {

	private String id;
	private String name;

	private OnlineJudge judge;
	private Integer solvedCount;

	public Problem(String id, String name, OnlineJudge judge, Integer solvedCount) {
		this.id = id;
		this.name = name;
		this.judge = judge;
		this.solvedCount = solvedCount;
	}

	public Problem(String id, String name, OnlineJudge judge) {
		this (id, name, judge, -1);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public OnlineJudge getJudge() {
		return judge;
	}

	public Integer getSolvedCount() {
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
				.key("judge").value(judge)
				.key("solvedCount").value(solvedCount)
			.endObject();

		return stringer.toString();
	}

}
