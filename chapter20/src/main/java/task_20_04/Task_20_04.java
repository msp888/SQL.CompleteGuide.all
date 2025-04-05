package task_20_04;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_20_04 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // удалить хранимую функцию, созданную в task_20_03
                String query = "drop function getEmployeePosition ";
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
