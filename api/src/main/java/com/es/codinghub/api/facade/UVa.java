package com.es.codinghub.api.facade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

public class UVa implements OnlineJudgeApi {

	private static final Resource api = new Resource("http://uhunt.felix-halim.net/api");
	private static final Object lock = new Object();

	private static Map<Integer, Problem> problemsByID;
	private static Map<Integer, Problem> problemsByNumber;
	private static JSONArray sugested;

	public UVa() throws IOException {
		synchronized (lock) {
			if (sugested == null) cacheProblems();
		}
	}

	@Override
	public List<Submission> getSubmissionsAfter(String username, Submission last) {
		try {
			String userid = api.request("/uname2uid/" + username);

			String response = (last == null) ?
					api.request("/subs-user/" + userid) :
					api.request("/subs-user/" + userid + "/" + last.getId());

			JSONArray subs = new JSONObject(response).getJSONArray("subs");
			List<Submission> result = new ArrayList<>();

			for (int i = 0; i < subs.length(); ++i) {
				JSONArray sub = subs.getJSONArray(i);
				result.add(createSubmission(sub));
			}

			return result;
		}

		catch (JSONException | IOException e) {
			return Collections.emptyList();
		}
	}

	@Override
	public JSONArray getSugestedProblems() {
		return sugested;
	}

	@Override
	public List<Contest> getUpcomingContests() throws IOException {
		String response = api.request("/contests");
		long timestamp = System.currentTimeMillis() / 1000L;

		JSONArray contests = new JSONArray(response);
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
		problemsByID = new HashMap<>();
		problemsByNumber = new HashMap<>();

		String response = api.request("/p");
		JSONArray probs = new JSONArray(response);

		for (int i = 0; i < probs.length(); ++i) {
			JSONArray prob = probs.getJSONArray(i);
			Problem problem = createProblem(prob);

			problemsByID.put(prob.getInt(0), problem);
			problemsByNumber.put(prob.getInt(1), problem);
		}

		response = api.request("/cpbook/3");
		sugested = parseBook(new JSONArray(response));
	}

	private static JSONArray parseBook(JSONArray book) {

		JSONArray result = new JSONArray();
		for (Object e : book) {

			String tag = null;
			JSONArray elems = null;

			if (e instanceof JSONObject) {
				JSONObject obj = (JSONObject) e;

				tag = obj.getString("title");
				elems = parseBook(obj.getJSONArray("arr"));
			}

			if (e instanceof JSONArray) {
				JSONArray arr = (JSONArray) e;
				JSONArray probs = new JSONArray();

				for (int i=1; i<arr.length(); ++i) {
					int number = Math.abs(arr.getInt(i));
					probs.put(problemsByNumber.get(number));
				}

				tag = arr.getString(0);
				elems = probs;
			}

			JSONObject chapter = new JSONObject();

			chapter.put("tag", tag);
			chapter.put("elements", elems);

			result.put(chapter);
		}

		return result;
	}

	private Problem createProblem(JSONArray prob) {
		return new Problem(
			Integer.toString(prob.getInt(1)),
			prob.getString(2),
			OnlineJudge.UVa,
			prob.getInt(18)
		);
	}

	private Submission createSubmission(JSONArray sub) {
		return new Submission(
			sub.getInt(0),
			sub.getInt(4),
			problemsByID.get(sub.getInt(1)),
			mapVerdict(sub.getInt(2))
		);
	}

	private Contest createContest(JSONObject c) {
		return new Contest(
			OnlineJudge.UVa,
			c.getString("name"),
			c.getLong("starttime"),
			c.getLong("endtime")-c.getLong("starttime")
		);
	}

	private Verdict mapVerdict(int ver) {
		switch (ver) {
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
