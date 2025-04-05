package task_14_04;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import task.utils.db.*;

public class Task_14_04 {


    public static void main(String[] args) throws IOException, SQLException {

        // удалить представление, созданное в task_14_02
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                String query =  "drop view view_QuantityInUserPack";
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
