package com.es.codinghub.api.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;

import com.es.codinghub.api.Database;
import com.es.codinghub.api.entities.Account;
import com.es.codinghub.api.entities.Submission;
import com.es.codinghub.api.entities.User;
import com.es.codinghub.api.facade.OnlineJudgeApi;

@Path("/user/{userid}/submissions")
public class SubmissionManager {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSugestedProblems(
			@PathParam("userid") long userid) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);
			List<Account> accounts = user.getAccounts();
			manager.getTransaction().commit();

			List<Submission> response = new ArrayList<>();

			for (Account account : accounts) {
				OnlineJudgeApi api = account.getJudge().getApi();
				List<Submission> submissions = api.getSubmissionsAfter(account.getUsername(), null);

				for (Submission submission : submissions)
					response.add(submission);
			}

			Collections.sort(response, new Comparator<Submission>() {
				@Override
				public int compare(Submission o1, Submission o2) {
					return o2.getTimestamp() - o1.getTimestamp();
				}
			});


			body = new JSONArray(response).toString();
			code = Status.OK;
		}

		catch (RollbackException e) {
			code = Status.INTERNAL_SERVER_ERROR;
		}

		catch (IOException e) {
			code = Status.SERVICE_UNAVAILABLE;
		}

		finally {
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}
}
