package org.demo.rest;
/*
MIT License

Copyright (c) 2025 IBM, Author: Thomas Weinzettl

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

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