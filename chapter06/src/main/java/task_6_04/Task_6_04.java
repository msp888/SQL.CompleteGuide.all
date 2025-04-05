package task_6_04;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_04 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // написать запрос, который возвращает константы: 76, 9.34, 'Слон'
                String query = "select 76, 9.34, 'Слон'";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // написать запрос, который возвращает константы: 76, 9.34, 'Слон' и столбец Name из таблицы tbl_Positions
                query = "select 76, 9.34, 'Слон', Name " +
                        "from tbl_Positions";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
