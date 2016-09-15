package com.es.codinghub.api;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.es.codinghub.api.facade.OnlineJudge;

@Path("/problems")
public class SugestionManager {

	@GET
	@Path("/{judge}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSugestedProblems(
			@PathParam("judge") OnlineJudge judge) {

		String body = null;
		Status code = null;

		try {
			body = judge.getApi().getSugestedProblems().toString();
			code = Status.OK;
		}

		catch (IOException e) {
			code = Status.SERVICE_UNAVAILABLE;
		}

		return Response.status(code).entity(body).build();
	}
}
