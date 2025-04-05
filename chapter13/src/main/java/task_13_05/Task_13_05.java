package task_13_05;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_05 {


    public static void main(String[] args) throws IOException, SQLException {

        // изменить в таблице PRODUCTS тип столбца "Регистрационное удостоверение" на varchar(50)
        //		Вывести содержимое таблицы PRODUCTS
        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                String query =  "ALTER TABLE PRODUCTS " +
                                            " DROP COLUMN Certificate ";
                stat.executeUpdate(query);

                query =         "ALTER TABLE PRODUCTS " +
                                            " ADD Certificate varchar(50) ";
                stat.executeUpdate(query);

                query = "select * from PRODUCTS";
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
