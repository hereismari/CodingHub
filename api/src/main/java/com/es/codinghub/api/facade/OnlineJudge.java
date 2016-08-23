package com.es.codinghub.api.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;

import com.es.codinghub.api.entities.Submission;

public abstract class OnlineJudge {

	private String domain;

	public OnlineJudge(String domain) {
		this.domain = domain;
	}

	protected String request(String resource) throws IOException {
		URL url = new URL(domain + resource);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()))) {
			return in.readLine();
		}
	}

	public abstract JSONArray getSubmissionsAfter(Submission last) throws IOException;

	public abstract JSONArray getSudgestedProblems() throws IOException;
}
