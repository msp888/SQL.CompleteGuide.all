package task_14_03;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import task.utils.db.*;

public class Task_14_03 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // выбрать все столбцы из в представления, созданного в task_14_02, для выбранной даты
                String query =  "select * " +
                                "from view_QuantityInUserPack  " +
                                "where DateOfOperation = '2024-03-02'";

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
