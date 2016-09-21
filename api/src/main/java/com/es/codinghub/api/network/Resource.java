package com.es.codinghub.api.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Resource {

	private String domain;

	public Resource(String domain) {
		this.domain = domain;
	}

	public String request(String resource) throws IOException {
		URL url = new URL(domain + resource);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()))) {
			return in.readLine();
		}
	}
}
