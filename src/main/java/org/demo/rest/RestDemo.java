package org.demo.rest;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import java.util.Optional;
import java.util.logging.Logger;

import org.demo.model.Coffee;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;

@Path("{name}")
public class RestDemo {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Coffee getTest(@PathParam("name") String name, @QueryParam("price") String price){
        Logger.getAnonymousLogger().info("getTest called");

        Optional<String> oprice = Optional.ofNullable(price);

        return new Coffee(1,name,oprice.orElse("$ 10.00"));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCoffee(@PathParam("name") String name, Coffee coffee){
        ResponseBuilder r;

        if ( ! name.equals(coffee.getName()) ){
            r = Response.noContent();
            r.status(301,"name is not equal");
        } else {
            r = Response.ok();
        }

        r.header("ReplyInfoName", name );
        r.header("coffeeName",coffee.getName());

        return r.build();
    }
}