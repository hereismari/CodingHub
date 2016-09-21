package com.es.codinghub.api;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Database {

	private static EntityManagerFactory factory =
			Persistence.createEntityManagerFactory("codinghub");

	public static EntityManager createEntityManager() {
		return factory.createEntityManager();
	}

	public static void close() {
		factory.close();
	}
}
