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
import org.json.JSONException;
import org.json.JSONObject;

import com.es.codinghub.api.entities.Contest;
import com.es.codinghub.api.entities.Problem;
import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.Verdict;
import com.es.codinghub.api.network.Resource;

public class Codeforces implements OnlineJudgeApi {

	private static final int SUGESTED_MAX = 5;

	private static final Resource api = new Resource("http://codeforces.com/api");
	private static final Object lock = new Object();

	private static Map<String, Problem> problems;
	private static JSONArray sugested;

	public Codeforces() throws IOException {
		synchronized (lock) {
			if (sugested == null) cacheProblems();
		}
	}

	@Override
	public List<Submission> getSubmissionsAfter(String username, Submission last) {
		try {
			int minTimestamp = last == null? -1 : last.getTimestamp();
			String response = api.request("/user.status?handle=" + username);

			JSONArray subs = new JSONObject(response).getJSONArray("result");
			List<Submission> result = new ArrayList<>();

			for (int i = 0; i < subs.length(); ++i) {
				JSONObject sub = subs.getJSONObject(i);
				Submission submission = createSubmission(sub);

				if (submission.getTimestamp() > minTimestamp
						&& submission.getProblem() != null)
					result.add(submission);
			} return result;
		}

		catch (IOException | JSONException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public JSONArray getSugestedProblems() {
		return sugested;
	}

	@Override
	public List<Contest> getUpcomingContests() throws IOException {
		String response = api.request("/contest.list");
		long timestamp = System.currentTimeMillis() / 1000L;

		JSONArray contests = new JSONObject(response).getJSONArray("result");
		List<Contest> result = new ArrayList<>();

		for (int i = 0; i < contests.length(); ++i) {
			JSONObject c = contests.getJSONObject(i);
			Contest contest = createContest(c);

			if (contest.getTimestamp() + contest.getDuration() > timestamp)
				result.add(contest);
		}

		return result;
	}

	private void cacheProblems() throws IOException {
		problems = new HashMap<>();
		JSONObject tags = new JSONObject();

		String response = api.request("/problemset.problems");
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

		parseTags(tags);
	}

	private void parseTags(JSONObject tags) {
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
			OnlineJudge.Codeforces,
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

	private Contest createContest(JSONObject c) {
		return new Contest(
			OnlineJudge.Codeforces,
			c.getString("name"),
			c.getLong("startTimeSeconds"),
			c.getLong("durationSeconds")
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
