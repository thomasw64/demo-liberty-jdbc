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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.demo.model.Coffee;
import jakarta.annotation.Resource;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("coffee")
public class CoffeeDb {
    @Resource(name = "jdbc/db2")
    public DataSource datasource;

    private static Logger logger = Logger.getLogger("coffee");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readFromDatabase() throws SQLException{
        ResponseBuilder response;
        List<Coffee> resultList = new ArrayList<>();

        try (
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "SELECT id,name,price,timestamp from COFFEE"
            );
        ) {
            
            ResultSet rs = statement.executeQuery();

            while( rs.next() ){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                Date timestamp = rs.getDate("timestamp");
                
                Coffee coffee = new Coffee(id, name, price);
                coffee.setTimestamp(timestamp.getTime());

                resultList.add(coffee);
            }

            response = Response.ok(resultList);
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.serverError();
        } 

        return response.build();
    }

    @Path("create-sample")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSampleData(){
        Coffee[] sampleCoffees = {
            new Coffee(1,"Arabica", "$ 2.50"),
            new Coffee(2,"Espresso", "$ 1.00")
        };

        ResponseBuilder response;

        try (
            Connection connection = datasource.getConnection();
        ) {
            if( !doesTableExists("COFFEE",connection) ) {
                createCoffeeTable(connection);
            }

            for ( Coffee coffee : sampleCoffees ) {
                insertIntoCoffee(coffee,connection);
            }

            response = Response.ok(sampleCoffees);
        } catch (SQLException e) {
            e.printStackTrace();
            response = Response.serverError();
        }

        return response.build();
    }


    private void insertIntoCoffee(Coffee coffee, Connection connection) throws SQLException {
        try (
            PreparedStatement insertStatement = connection.prepareStatement(
                "INSERT INTO COFFEE VALUES(?,?,?,?)"
            );
        ) {
            insertStatement.setLong(1, coffee.id);
            insertStatement.setString(2, coffee.getName());
            insertStatement.setString(3, coffee.getPrice());
            insertStatement.setDate(4, new Date(coffee.getTimestamp()));

            insertStatement.executeUpdate();
            
        } 
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertCoffee(Coffee coffee) throws SQLException {
        CoffeeDb.logger.info("insertCoffee");
        ResponseBuilder response;
        try (
            Connection connection = datasource.getConnection();
        ) {
            long freeId = getFreeId(connection);

            if ( coffee.id < freeId ) coffee.id = freeId;

            insertIntoCoffee(coffee, connection);
            response = Response.ok(coffee,MediaType.APPLICATION_JSON);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.serverError();
        }

        return response.build();
    }

    private boolean doesTableExists(String tableName, Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(
            null, 
            null, 
            tableName, 
            null
        );

        return resultSet.next();
    }

    private void createCoffeeTable(Connection connection) throws SQLException{
        try (
            PreparedStatement createStatement = connection.prepareStatement(
                """
                CREATE TABLE COFFEE (id INT NOT NULL PRIMARY KEY, name VARCHAR(128),
                price VARCHAR(10), timestamp DATE )"""
            );
        ) {
            createStatement.execute();  
        } 
    }

    private long getFreeId(Connection connection) throws SQLException {
        long freeId = 0l;
        try (
            PreparedStatement selectMaxStatement = connection.prepareStatement(
                "SELECT MAX(id) FROM COFFEE"
            );
        ) {
            ResultSet resultSet = selectMaxStatement.executeQuery();
            if ( resultSet.next() ){
                freeId = resultSet.getLong(1) + 1;
            }
        }
        return freeId;
    }
}
