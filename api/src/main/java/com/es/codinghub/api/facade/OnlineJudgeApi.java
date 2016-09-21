package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import com.es.codinghub.api.entities.Submission;

public interface OnlineJudgeApi {

	public abstract List<Submission> getSubmissionsAfter(String username, Submission last) throws IOException;
	public abstract JSONArray getSugestedProblems() throws IOException;
}
