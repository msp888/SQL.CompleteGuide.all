package task_13_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_01 {

    public static void main(String[] args) throws IOException {

        try (Connection conn = DBUtils.getConnectionDBMS())
        {
            // создать базу данных: ed2
            try (Statement stat = conn.createStatement())
            {
                String query =  "create database ed2 ";
                stat.executeUpdate(query);
            }

        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
