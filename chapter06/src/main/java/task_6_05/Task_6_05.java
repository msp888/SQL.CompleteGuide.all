package task_6_05;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_05 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести значения столбцов PackagesInBox, BoxesOnPallet таблицы tbl_Products
                String query = "select PackagesInBox, BoxesOnPallet " +
                               "from tbl_Products";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести уникальные, не повторяющиеся сочетания значений,
                // столбцов PackagesInBox, BoxesOnPallet таблицы tbl_Products.
                query = "select distinct PackagesInBox, BoxesOnPallet " +
                        "from tbl_Products";
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
