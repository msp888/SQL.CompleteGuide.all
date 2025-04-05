package task_9_04;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_9_04 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection("educational"))
        {
            try (Statement stat = conn.createStatement())
            {
                // запрос со страницы 230
                String query =  "SELECT CITY " +
                                "FROM OFFICES " +
                                "where TARGET > (SELECT sum(QUOTA) " +
                                                "FROM SALESREPS " +
                                                "where REP_OFFICE = OFFICE)";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // переписать запрос со страницы 230 (вариант 1)
                query = "SELECT o.CITY, o.TARGET, t.resQUOTA " +
                        "FROM OFFICES o inner join " +
                            "(SELECT sum(QUOTA) as resQUOTA, REP_OFFICE " +
                            " FROM SALESREPS " +
                            " group by REP_OFFICE) t " +
                        "on t.REP_OFFICE = o.OFFICE " +
                        "where o.TARGET > t.resQUOTA";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // переписать запрос со страницы 230 (вариант 2)
                query = "SELECT o.CITY, o.TARGET, t.resQUOTA " +
                        "FROM OFFICES o inner join " +
                        "(SELECT sum(QUOTA) as resQUOTA, REP_OFFICE " +
                        " FROM SALESREPS " +
                        " group by REP_OFFICE) t " +
                        "on t.REP_OFFICE = o.OFFICE and o.TARGET > t.resQUOTA ";
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
