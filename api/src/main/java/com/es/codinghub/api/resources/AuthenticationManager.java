package com.es.codinghub.api.resources;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONObject;

import com.es.codinghub.api.Database;
import com.es.codinghub.api.Mailing;
import com.es.codinghub.api.entities.User;

import java.math.BigInteger;
import java.security.SecureRandom;

@Path("/auth")
public class AuthenticationManager {

	private static SecureRandom random = new SecureRandom();

	@GET
	@Path("/login")
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

	@POST
	@Path("/recover")
	@Consumes("application/x-www-form-urlencoded")
	public Response recover(
			@FormParam("email") String email) {

		EntityManager manager = Database.createEntityManager();

		String body = null;
		Status code = null;

		manager.getTransaction().begin();

		try {
			String query = "FROM User WHERE email=:email";

			User user = manager.createQuery(query, User.class)
					.setParameter("email", email)
					.getSingleResult();

			String password = generatePassword();
			Mailing.send(email, "Recupera\u00e7\u00e3o de conta",
					"Sua nova senha \u00e9: " + password);
			user.setPassword(password);

			code = Status.OK;
		}

		catch (NoResultException e) {
			code = Status.BAD_REQUEST;
		}

		catch (MessagingException e) {
			code = Status.SERVICE_UNAVAILABLE;
		}

		finally {
			manager.getTransaction().commit();
			manager.close();
		}

		return Response.status(code).entity(body).build();
	}

	private String generatePassword() {
		return new BigInteger(60, random).toString(32);
	}
}
