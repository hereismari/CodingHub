package com.es.codinghub.api;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

@Path("/login")
public class AuthenticationManager {

	@GET
	public Response login(
			@HeaderParam("email") String email,
			@HeaderParam("password") String password) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		manager.getTransaction().begin();

		try {
			String query = "SELECT id FROM User WHERE email=:email AND password=:password";

			Long userid = manager.createQuery(query, Long.class)
					.setParameter("email", email)
					.setParameter("password", password)
					.getSingleResult();

			JSONObject response = new JSONObject();
			response.put("userid", userid);

			body = response.toString();
			code = Status.OK;
		}

		catch (NoResultException e) {
			code = Status.UNAUTHORIZED;
		}

		finally {
			manager.getTransaction().commit();
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}
}
