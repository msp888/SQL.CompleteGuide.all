package task_20_06;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_20_06 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // удалить хранимую процедуру, созданную в task_20_05
                String query = "drop procedure sp_EmployeeInfo ";
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
