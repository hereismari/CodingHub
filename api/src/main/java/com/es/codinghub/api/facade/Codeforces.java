package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.es.codinghub.api.entities.Problem;
import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.Verdict;

public class Codeforces extends OnlineJudge {

	private String userHandle;

	private static Map<String, Problem> problems;

	private static JSONObject tags;
	private static JSONArray sugested;

	private static final int SUGESTED_MAX = 5;

	public Codeforces(String username) throws IOException {
		super("http://codeforces.com/api/");
		this.userHandle = username;

		if (sugested == null) cache();
	}

	@Override
	public JSONArray getSubmissionsAfter(Submission last) throws IOException {
		String response = request("user.status?handle=" + userHandle);
		Integer minTimestamp = last == null? -1 : last.getTimestamp();

		JSONArray subs = new JSONObject(response).getJSONArray("result");
		JSONArray result = new JSONArray();

		for (int i = 0; i < subs.length(); ++i) {
			JSONObject sub = subs.getJSONObject(i);
			Submission submission = createSubmission(sub);

			if (submission.getTimestamp() > minTimestamp
					&& submission.getProblem() != null)
				result.put(submission);
		}

		return result;
	}

	@Override
	public JSONArray getSugestedProblems() {
		return sugested;
	}

	private void cache() throws IOException {
		problems = new HashMap<>();
		tags = new JSONObject();

		String response = request("/problemset.problems");
		JSONObject json = new JSONObject(response).getJSONObject("result");

		JSONArray probs = json.getJSONArray("problems");
		JSONArray stats = json.getJSONArray("problemStatistics");

		for (int i = 0; i < probs.length(); ++i) {
			JSONObject p = probs.getJSONObject(i);
			JSONObject s = stats.getJSONObject(i);

			Problem problem = createProblem(p, s);
			problems.put(problem.getId(), problem);

			JSONArray t = p.getJSONArray("tags");

			for (int j = 0; j < t.length(); ++j) {
				String tag = t.getString(j);
				tags.append(tag, problem);
			}
		}

		parseTags();
	}

	private void parseTags() {
		sugested = new JSONArray();
		Iterator<String> it = tags.keys();

		while (it.hasNext()) {
			List<Problem> filter = new ArrayList<>();

			String tag = it.next();
			JSONArray elem = tags.getJSONArray(tag);

			for (int i = 0; i < elem.length(); ++i) {
				Problem prob = (Problem) elem.get(i);
				filter.add(prob);
			}

			Collections.sort(filter, new Comparator<Problem>() {
				@Override
				public int compare(Problem o1, Problem o2) {
					return o2.getSolvedCount() - o1.getSolvedCount();
				}
			});

			int max = Math.min(filter.size(), SUGESTED_MAX);
			filter = filter.subList(0, max);

			Collections.sort(filter, new Comparator<Problem>() {
				@Override
				public int compare(Problem o1, Problem o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			elem = new JSONArray(filter);
			JSONObject chapter = new JSONObject();

			chapter.put("tag", tag);
			chapter.put("elements", elem);

			sugested.put(chapter);
		}
	}

	private String buildProblemId(JSONObject prob) {
		return prob.getInt("contestId") + prob.getString("index");
	}

	private Problem createProblem(JSONObject prob, JSONObject stats) {
		return new Problem(
			buildProblemId(prob),
			prob.getString("name"),
			stats.getInt("solvedCount")
		);
	}

	private Submission createSubmission(JSONObject sub) {
		JSONObject prob = sub.getJSONObject("problem");
		String id = buildProblemId(prob);

		return new Submission(
			sub.getInt("id"),
			sub.getInt("creationTimeSeconds"),
			problems.get(id),
			mapVerdict(sub.getString("verdict"))
		);
	}

	private Verdict mapVerdict(String ver) {
		switch (ver) {
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
