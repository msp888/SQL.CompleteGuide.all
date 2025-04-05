package task_10_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_10_01 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // написать запрос, который возвращает список должностей с указанием количества рабочих мест
                //	для каждой должности и количество свободных рабочих мест для каждой должности.
                //	Для информации: может пригодиться выражение COALESCE()
                String query =  "select p.Name, p.Workplaces, (p.Workplaces - COALESCE(e.used, 0)) as freePlaces " +
                                "from tbl_Positions p left outer join " +
                                        "(select ID_Position, count(ID_Position) as used " +
                                        " from tbl_Employees " +
                                        " group by ID_Position " +
                                        ") e " +
                                    "on p.ID_Position = e.ID_Position ";

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
