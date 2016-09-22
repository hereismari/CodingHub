package com.es.codinghub.api.entities;

import org.json.JSONString;
import org.json.JSONStringer;
import org.json.JSONWriter;

import java.security.InvalidParameterException;

public class Submission implements JSONString {

	private int id;
	private int timestamp;

	private Problem problem;
	private Verdict verdict;

	public Submission(int id, int timestamp, Problem problem, Verdict verdict) {

		if(id < 0 || timestamp < 0 || problem == null || verdict == null)
			throw new InvalidParameterException();

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

	public Problem getProblem() {
		return problem;
	}

	public Verdict getVerdict() {
		return verdict;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Submission == false)
			return false;
		Submission other = (Submission) obj;
		return id == other.id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
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
