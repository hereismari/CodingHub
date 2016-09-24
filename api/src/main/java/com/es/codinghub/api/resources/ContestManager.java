package com.es.codinghub.api.resources;

import com.es.codinghub.api.entities.Contest;
import com.es.codinghub.api.facade.OnlineJudge;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

@Path("/contests")
public class ContestManager {

    private static ResponseBuilder cache;
    private static Object lock = new Object();

    private static long timestamp = 0;
    private static long threshold = 43200;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpcomingContests() {
        synchronized (lock) {

            long now = System.currentTimeMillis() / 1000L;
            if (now - timestamp < threshold) return cache.build();

            String body = null;
            Status code = null;

            try {
                List<Contest> result = new ArrayList<>();

                for (OnlineJudge judge : OnlineJudge.values()) {
                    List<Contest> contests = judge.getApi().getUpcomingContests();
                    result.addAll(contests);
                }

                Collections.sort(result, new Comparator<Contest>() {
                    @Override
                    public int compare(Contest o1, Contest o2) {
                        return Long.compare(o1.getTimestamp(), o2.getTimestamp());
                    }
                });

                JSONArray response = new JSONArray();
                for (Contest c : result) response.put(c);

                body = response.toString();
                code = Response.Status.OK;

                cache = Response.status(code).entity(body);
                timestamp = now;
            }

            catch (IOException e) {
                code = Response.Status.SERVICE_UNAVAILABLE;
            }

            return Response.status(code).entity(body).build();
        }
    }
}
