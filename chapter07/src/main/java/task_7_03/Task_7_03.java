package task_7_03;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_7_03 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести список сотрудников (фамилия, имя, отчество) и должности, которые они занимают
                String query = "select Surname, e.Name, MiddleName, p.Name as Position " +
                               "from tbl_Employees e join tbl_Positions p " +
                                    "on e.ID_Position = p.ID_Position";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести информацию о датчиках с указанием наименования зоны хранения
                query = "select st.Name, Position, Designation, MinValue, MaxValue, Inspection " +
                        "from tbl_Sensors s join tbl_StorageAreas st " +
                        "on s.ID_StorageArea = st.ID_StorageArea ";
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
