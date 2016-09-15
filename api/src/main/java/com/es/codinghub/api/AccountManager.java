package com.es.codinghub.api;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.es.codinghub.api.entities.User;
import com.es.codinghub.api.facade.OnlineJudge;

@Path("/user/{userid}/account")
public class AccountManager {

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response addAccount(
			@PathParam("userid") long userid,
			@FormParam("judge") OnlineJudge judge,
			@FormParam("username") String username) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);

			user.addAccount(judge, username);
			manager.getTransaction().commit();

			code = Status.CREATED;
		}

		catch (RollbackException e) {
			code = Status.BAD_REQUEST;
		}

		finally {
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}

	@DELETE
	@Path("/{accountid}")
	public Response removeAccount(
			@PathParam("userid") long userid,
			@PathParam("accountid") long accountid) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);

			if (user == null)
				code = Status.NOT_FOUND;

			else {
				user.removeAccount(accountid);
				code = Status.OK;
			}

			manager.getTransaction().commit();
		}

		catch (RollbackException e) {
			code = Status.INTERNAL_SERVER_ERROR;
		}

		finally {
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}
}
