package com.es.codinghub.api.resources;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.es.codinghub.api.Database;
import com.es.codinghub.api.entities.User;

@Path("/user")
public class UserManager {

	@POST
	@Consumes("application/x-www-form-urlencoded")
	public Response createUser(
			@FormParam("email") String email,
			@FormParam("password") String password) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = new User(email, password);

			manager.persist(user);
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
	@Path("/{userid}")
	public Response deleteUser(
			@PathParam("userid") long userid) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);

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
	@Path("/{userid}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(
			@PathParam("userid") long userid) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);

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

	@PUT
	@Path("/{userid}/password")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response updatePassword(String password,
			@PathParam("userid") long userid) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		try {
			manager.getTransaction().begin();
			User user = manager.find(User.class, userid);

			if (user == null)
				code = Status.NOT_FOUND;

			else {
				user.setPassword(password);
				code = Status.OK;
			}

			manager.getTransaction().commit();
		}

		catch (RollbackException e) {
			code = Status.BAD_REQUEST;
		}

		finally {
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}
}
