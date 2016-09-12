package com.es.codinghub.api;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.es.codinghub.api.entities.User;

@Path("/user")
public class UserManager {

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createUser(
			@FormParam("email") String email,
			@FormParam("password") String password) {

		EntityManager manager = Database.createEntityManager();

		Status code = null;
		String body = null;

		try {
			manager.getTransaction().begin();
			User user = new User(email, password);

			manager.persist(user);
			manager.getTransaction().commit();

			code = Status.CREATED;
		}

		catch (RollbackException e) {
			code = Status.CONFLICT;
		}

		finally {
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}

	@DELETE
	@Path("/{id}")
	public Response deleteUser(
			@PathParam("id") long id) {

		EntityManager manager = Database.createEntityManager();

		Status code = null;
		String body = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, id);

			if (user == null)
				code = Status.NOT_FOUND;

			else {
				manager.remove(user);
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

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(
			@PathParam("id") long id) {

		EntityManager manager = Database.createEntityManager();

		Status code = null;
		String body = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, id);

			if (user == null)
				code = Status.NOT_FOUND;

			else {
				body = user.toJSONString();
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
