package task_13_07;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_07 {

    public static void main(String[] args) throws IOException {

        try (Connection conn = DBUtils.getConnectionDBMS())
        {
            // удалить БД ed2
            try (Statement stat = conn.createStatement())
            {
                String query =  "drop database ed2 ";
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
