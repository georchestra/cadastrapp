package org.georchestra.cadastrapp.service;


import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.georchestra.cadastrapp.JsonBean;


@Path("/getCommune")
public class Commune {
	
	@Resource(name="jdbc/cadastrapp")
	private DataSource dataSource;

    @GET
    @Path("/id/{input}")
    @Produces("text/plain")
    public String ping(@PathParam("input") String input) throws SQLException {
    	
    	Connection connection = dataSource.getConnection();
    	
        return input;
    }

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    @Path("/jsonBean")
    public Response modifyJson(JsonBean input) {
        input.setVal2(input.getVal1());
        return Response.ok().entity(input).build();
    }
}

