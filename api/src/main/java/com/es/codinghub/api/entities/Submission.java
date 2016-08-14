package com.es.codinghub.api.entities;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

public class Submission implements JSONString {
	private int id;
	private int timestamp;
	private String problem;
	private Verdict verdict;
	
	public Submission(int id, int timestamp, String problem, Verdict verdict) {
		this.id = id;
		this.timestamp = timestamp;
		this.problem = problem;
		this.verdict = verdict;
	}
	
	public int getId() {
		return id;
	}
	
	public int getTimestamp() {
		return timestamp;
	}
	
	public String getProblem() {
		return problem;
	}
	
	public Verdict getVerdict() {
		return verdict;
	}

	@Override
	public String toJSONString() {
		JSONWriter stringer = new JSONStringer()
			.object()
				.key("id").value(id)
				.key("timestamp").value(timestamp)
				.key("problem").value(problem)
				.key("verdict").value(verdict)
			.endObject();
		
		return stringer.toString();
	}
	
}
