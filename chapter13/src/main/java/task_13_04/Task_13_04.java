package task_13_04;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_04 {


    public static void main(String[] args) throws IOException, SQLException {

        // добавить в таблицу PRODUCTS столбцы:
        //			а) "Торговое наименование" - типа varchar(50)
        //			б) "Регистрационное удостоверение" - типа int
        //		Вывести содержимое таблицы PRODUCTS
        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                String query =  "ALTER TABLE PRODUCTS " +
                                            " ADD TradeName varchar(50) ";
                stat.executeUpdate(query);

                query =         "ALTER TABLE PRODUCTS " +
                                            " ADD Certificate int ";
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
