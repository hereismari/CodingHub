package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.es.codinghub.api.entities.Problem;
import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.Verdict;

public class Codeforces extends OnlineJudge {

	private String userHandle;
	private static Map<String, Problem> problems;

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
			JSONObject prob = sub.getJSONObject("problem");
			String id = ((Integer)prob.get("contestId")).toString() + (String)prob.get("index");
			System.out.println(id);
			Problem problem = problems.get(id);
			if(problem != null) {
				result.put(createSubmission(sub, problem)); 
		
			}
		}
		return result;
	}

	@Override
	public JSONObject getSudgestedProblems() {
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
		JSONArray probstats = new JSONObject(response).getJSONObject("result").getJSONArray("problemStatistics");
		
		for(int i = 0; i < probs.length(); ++i) {
			
			JSONObject prob = probs.getJSONObject(i);
			
			String name = (String)prob.get("name");
			String id = ((Integer)prob.get("contestId")).toString() + (String)prob.get("index");
			
			JSONObject stats = probstats.getJSONObject(i);
			problems.put(id, createProblem(id, name, stats));
		}
	}
	
	private Problem createProblem(String id, String name, JSONObject stats) {	
		return new Problem(id, name, (Integer)stats.get("solvedCount"));
	}
	
	private Submission createSubmission(JSONObject sub, Problem problem) {
		return new Submission(
				(int) sub.get("id"),
				(int) sub.get("creationTimeSeconds"),
				problem,
				mapVerdict((String) sub.get("verdict"))
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
