package org.demo.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("db")
public class DbDemo {
    @Resource(name = "jdbc/db2")
    public DataSource datasource;

    @Path("sysdummy")
    @GET
    public String sysdummy() {
        StringBuilder result = new StringBuilder();

        try (
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT 1 FROM SYSIBM.SYSDUMMY1");
        ) {
            ResultSet resultSet = statement.executeQuery();
            
            while( resultSet.next() ){
                result.append("ok");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.append("failed (%s)".formatted(e.toString()));
        }
        result.append("\n");

        return result.toString();
    }

    @GET
    public String readFromDatabase() throws SQLException{
        StringBuilder result = new StringBuilder();

        try (
            Connection connection = datasource.getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * from SYS.SYSTABLES");
        ) {
            
            ResultSet rs = statement.executeQuery();

            while( rs.next() ){
                result.append(rs.getString(1));
                result.append(", ");
                result.append(rs.getString(2));
                result.append(", ");
                result.append(rs.getString(3));
                result.append(", ");
                result.append(rs.getString(4));
                result.append(", ");
                result.append(rs.getString(5));
                result.append('\n');
            }

        } catch (SQLException e) {
            e.printStackTrace();
            result.append(String.format("%nException occured. (%s)",e.toString()));
        } 

        return result.toString();
    }

    @Path("create")
    @GET
    public String createSampleData(){
        StringBuilder result = new StringBuilder();

        try (
            Connection connection = datasource.getConnection();
            // PreparedStatement create_schema_statement = connection.prepareStatement("CREATE SCHEMA USER1");
            PreparedStatement create_statement = connection.prepareStatement(
                "CREATE TABLE USER1.book (id INT NOT NULL GENERATED ALWAYS AS IDENTITY, title VARCHAR(128), PRIMARY KEY(ID))"
            );
            // PreparedStatement insert_statement = connection.prepareStatement(
            //     "INSERT INTO book VALUES(?,?)"
            // );
        ) {
            // boolean schemaCreated = create_schema_statement.execute();
            // result.append("Schema was created %d \n".formatted(schemaCreated));

            boolean created = create_statement.execute();

            result.append("book created (%d)\n".formatted(created));

            // insert_statement.setInt(1,1);
            // insert_statement.setString(2, "A book");
            // insert_statement.executeUpdate();
            result.append("successfuly done.\n");
        } catch (Exception e) {
            e.printStackTrace();
            result.append("Exception: %s\n".formatted(e.toString()));
        }

        result.append("====\n");
        return result.toString();
    }
}
