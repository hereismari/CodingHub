package com.es.codinghub.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

import com.es.codinghub.api.facade.OnlineJudge;

@Path("/problems")
public class SugestionManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSugestedProblems() {

		String body = null;
		Status code = null;

		try {
			JSONArray response = new JSONArray();

			for (OnlineJudge judge : OnlineJudge.values()) {
				JSONObject chapter = new JSONObject();

				String tag = judge.toString();
				JSONArray elements = judge.getApi().getSugestedProblems();

				chapter.put("tag", tag);
				chapter.put("elements", elements);

				response.put(chapter);
			}

			body = response.toString();
			code = Status.OK;
		}

		catch (IOException e) {
			code = Status.SERVICE_UNAVAILABLE;
		}

		return Response.status(code).entity(body).build();
	}
}
