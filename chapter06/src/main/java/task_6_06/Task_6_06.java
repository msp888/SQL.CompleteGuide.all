package task_6_06;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_06 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести строки таблицы tbl_StorageAreaItems,
                //	  для которых значение столбца ID_StorageAreaItem находится в диапазоне от 24 до 34.
                //	  При формировании условия применять оператор BETWEEN...AND
                String query = "select * " +
                               "from tbl_StorageAreaItems " +
                               "where ID_StorageAreaItem between 24 and 34";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products,
                // для которых значение столбца ID_Product НЕ принадлежат диапазону от 7 до 20.
                // При формировании условия применять оператор NOT BETWEEN...AND
                query = "select * " +
                        "from tbl_Products " +
                        "where ID_Product not between 7 and 20";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products,
                //	  для которых значение столбца ID_Product равны от 3, 7, 12, 20, 21.
                //	  При формировании условия применять оператор IN
                query = "select * " +
                        "from tbl_Products " +
                        "where ID_Product in (3, 7, 12, 20, 21)";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products,
                //	  для которых значение столбца Certificate начинается с символов 'ЛСР-'.
                //	  При формировании условия применять оператор LIKE
                query = "select * " +
                        "from tbl_Products " +
                        "where Certificate like 'ЛСР-%'";
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
