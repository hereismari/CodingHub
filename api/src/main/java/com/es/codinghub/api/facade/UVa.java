package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.Verdict;

public class UVa extends OnlineJudge {
	
	private String userID;
	private static Map<Integer, String> problems;

	public UVa(String username) throws IOException {
		super("http://uhunt.felix-halim.net/api");
		this.userID = request("/uname2uid/" + username);
		if (problems == null) cacheProblems();
	}
	
	@Override
	public JSONArray getSubmissionsAfter(Submission last) throws IOException {
		String response = (last == null)?
			request("/subs-user/" + userID):
			request("/subs-user/" + userID + "/" + last.getId());
		
		JSONArray subs = new JSONObject(response).getJSONArray("subs");
		JSONArray result = new JSONArray();
		
		for(int i = 0; i < subs.length(); ++i) {
			JSONArray sub = subs.getJSONArray(i);
			result.put(createSubmission(sub)); 
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
			UVa uva = new UVa("VictorAA");
			System.out.println("Requesting submissions...");
			JSONArray result = uva.getSubmissionsAfter(null);
			System.out.println(result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void cacheProblems() throws IOException {
		problems = new HashMap<>();
		
		String response = request("/p");
		JSONArray probs = new JSONArray(response);
		
		for(int i = 0; i < probs.length(); ++i) {
			JSONArray prob = probs.getJSONArray(i);
			String title = prob.getInt(1) + " - " + prob.getString(2);
			problems.put(prob.getInt(0), title);
		}
	}
	
	private Submission createSubmission(JSONArray sub) {
		return new Submission(
			sub.getInt(0),
			sub.getInt(4),
			problems.get(sub.getInt(1)),
			mapVerdict(sub.getInt(2))
		);
	}
	
	private Verdict mapVerdict(int ver) {
		switch(ver) {
			case 30: return Verdict.COMPILATION_ERROR;
			case 40: return Verdict.RUNTIME_ERROR;
			case 50: return Verdict.TIME_LIMIT;
			case 60: return Verdict.MEMORY_LIMIT;
			case 70: return Verdict.WRONG_ANSWER; 
			case 80: return Verdict.PRESENTATION_ERROR;
			case 90: return Verdict.ACCEPTED;
			default: return Verdict.OTHER;
		}
	}
}
