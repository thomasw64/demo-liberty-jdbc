package org.demo.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import jakarta.annotation.Resource;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("sysdummy")
public class SysDummy {
    @Resource(name = "jdbc/db2")
    private DataSource dataSource;

    @GET
    public String getSysDummy() {
        StringBuilder result = new StringBuilder();

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM SYSIBM.SYSDUMMY1");
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while( resultSet.next() ){
                result.append("ok");
            }
            result.append("\n");
        } catch (SQLException e) {
            result.append("Exception: %s%n".formatted(e.toString()));
        }

        return result.toString();
    }
}
