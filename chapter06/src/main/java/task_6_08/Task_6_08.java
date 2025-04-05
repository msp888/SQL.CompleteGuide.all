package task_6_08;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_08 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                //объединить все строки tbl_Positions для столбцов ID_Position, Name
                //	  и все строки tbl_TransportUnits для столбцов ID_TransportUnit, Name
                String query =
                        "select ID_Position, Name " +
                        "from tbl_Positions " +
                        "union all " +
                        "select 0, '------' " +
                        "union all " +
                        "select ID_TransportUnit, Name " +
                        "from tbl_TransportUnits ";
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
