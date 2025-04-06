package task_6_01;

import task.utils.db.*;
import java.io.*;
import java.sql.*;
//import com.microsoft.sqlserver.jdbc.*;

public class Task_6_01 {

    public static void main(String[] args) throws IOException, SQLException
    {
        System.out.println("Изучаем Git...");
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести все строки таблицы tbl_Employees
                String query = "select * from tbl_Employees";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products, для которых значение столбца PackagesInBox равно 10
                query = "select * from tbl_Products where PackagesInBox = 10";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_StorageAreaItems, для которых значение столбца ID_StorageAreaItem находится в диапазоне от 24 до 34.
                query = "select * from tbl_StorageAreaItems where ID_StorageAreaItem >= 24 and ID_StorageAreaItem <= 34";
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
