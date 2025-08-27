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

import javax.sql.DataSource;

import org.demo.model.Coffee;

import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("coffee")
public class CoffeeDb {
    @Resource(name = "jdbc/db2")
    public DataSource datasource;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Coffee> readFromDatabase() throws SQLException{
        List<Coffee> result = new ArrayList<>();

        try (
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT id,name,price,timestamp from COFFEE");
        ) {
            
            ResultSet rs = statement.executeQuery();

            while( rs.next() ){
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String price = rs.getString("price");
                Date timestamp = rs.getDate("timestamp");
                
                Coffee coffee = new Coffee(id, name, price);
                coffee.setTimestamp(timestamp.getTime());

                result.add(coffee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } 

        return result;
    }

    @Path("create-sample")
    @GET
    public String createSampleData(){
        Coffee[] sampleCoffees = {
            new Coffee(1,"Arabica", "$ 2.50"),
            new Coffee(2,"Espresso", "$ 1.00")
        };

        StringBuilder result = new StringBuilder();

        try (
            Connection connection = datasource.getConnection();
        ) {
            if( !doesTableExists("COFFEE",connection) ) createCoffeeTable(connection);

            for (Coffee coffee : sampleCoffees) {
                insertIntoCoffee(coffee,connection);
            }

            result.append("successfuly done.\n");
        } catch (SQLException e) {
            e.printStackTrace();
            result.append("Exception: %s\n".formatted(e.toString()));
        }

        result.append("====\n");
        return result.toString();
    }


    private void insertIntoCoffee(Coffee coffee, Connection connection) throws SQLException {
        try (
            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO COFFEE VALUES(?,?,?,?)");
        ) {
            insertStatement.setLong(1, coffee.id);
            insertStatement.setString(2, coffee.getName());
            insertStatement.setString(3, coffee.getPrice());
            insertStatement.setDate(4, new Date(coffee.getTimestamp()));

            insertStatement.executeUpdate();
            
        } 
    }

    private boolean doesTableExists(String tableName, Connection connection) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet resultSet = metaData.getTables(null, null, tableName, null);

        return resultSet.next();
    }

    private void createCoffeeTable(Connection connection) throws SQLException{
        try (
            PreparedStatement createStatement = connection.prepareStatement(
                "CREATE TABLE COFFEE (id INT NOT NULL PRIMARY KEY, name VARCHAR(128), price VARCHAR(10), timestamp DATE )"
            );
        ) {
            createStatement.execute();  
        } 
    }
}
