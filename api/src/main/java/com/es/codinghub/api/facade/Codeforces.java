package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.Verdict;

public class Codeforces extends OnlineJudge {

	private String userHandle;
	private static Map<String, String> problems;

	public Codeforces(String username) throws IOException {
		super("http://codeforces.com/api/");
		this.userHandle = username;
		if (problems == null) cacheProblems();
	}

	@Override
	public JSONArray getSubmissionsAfter(Submission last) throws IOException {
		String response = request("user.status?handle=" + userHandle);
		JSONArray subs = new JSONObject(response).getJSONArray("result");

		JSONArray result = new JSONArray();

		for(int i = 0; i < subs.length(); ++i) {
			JSONObject sub = subs.getJSONObject(i);
			result.put(createSubmission(sub));
		}

		return result;
	}

	@Override
	public JSONArray getSudgestedProblems() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		try {
			Codeforces cf = new Codeforces("marianneL");
			System.out.println("Requesting submissions...");
			JSONArray result = cf.getSubmissionsAfter(null);
			System.out.println(result);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void cacheProblems() throws IOException {
		problems = new HashMap<>();

		String response = request("/problemset.problems");
		JSONArray probs = new JSONObject(response).getJSONObject("result").getJSONArray("problems");

		for(int i = 0; i < probs.length(); ++i) {
			JSONObject prob = probs.getJSONObject(i);
			problems.put(prob.getInt("contestId") + prob.getString("index"), prob.getString("name"));
		}
	}

	private Submission createSubmission(JSONObject sub) {
		return new Submission(
			sub.getInt("id"),
			sub.getInt("creationTimeSeconds"),
			sub.getJSONObject("problem").getString("name"),
			mapVerdict(sub.getString("verdict"))
		);
	}

	private Verdict mapVerdict(String ver) {
		switch(ver) {
			case "OK": return Verdict.ACCEPTED;
			case "TIME_LIMIT_EXCEEDED": return Verdict.TIME_LIMIT;
			case "MEMORY_LIMIT_EXCEEDED": return Verdict.MEMORY_LIMIT;
			case "PRESENTATION_ERROR": return Verdict.PRESENTATION_ERROR;
			case "RUNTIME_ERROR": return Verdict.RUNTIME_ERROR;
			case "COMPILATION_ERROR": return Verdict.COMPILATION_ERROR;
			case "WRONG_ANSWER": return Verdict.WRONG_ANSWER;
			default: return Verdict.OTHER;
		}
	}
}
