package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;

import com.es.codinghub.api.entities.Contest;
import com.es.codinghub.api.entities.Submission;

public interface OnlineJudgeApi {

	List<Contest> getUpcomingContests() throws IOException;
	List<Submission> getSubmissionsAfter(String username, Submission last) throws IOException;
	JSONArray getSugestedProblems() throws IOException;
}
