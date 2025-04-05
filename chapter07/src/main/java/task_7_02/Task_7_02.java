package task_7_02;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_7_02 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // написать запрос, который возвращает константы: 76, 9.34, 'Слон'
                String query = "select 76 as c1, 9.34 as c2, 'Слон' as c3";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // написать запрос, который возвращает константы: 76, 9.34, 'Слон' и столбец Name из таблицы tbl_Positions
                query = "select 76 as c1, 9.34 as c2, 'Слон' as c3, Name " +
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
