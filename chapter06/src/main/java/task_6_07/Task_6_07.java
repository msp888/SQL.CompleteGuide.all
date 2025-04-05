package task_6_07;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_07 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести строки таблицы tbl_Products,
                //	  для которых значение столбца ID_Product НЕ принадлежат диапазону от 7 до 20.
                //	  Отсортировать по возрастанию в столбце PackagesInBox
                //	  При формировании запроса применять оператор ORDER ВУ
                String query = "select * " +
                        "from tbl_Products " +
                        "where ID_Product not between 7 and 20 " +
                        "order by PackagesInBox";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products,
                //	  для которых значение столбца ID_Product НЕ принадлежат диапазону от 7 до 20.
                //	  Отсортировать по возрастанию в столбцах PackagesInBox, TradeName
                //	  При формировании запроса применять оператор ORDER ВУ
                query = "select * " +
                        "from tbl_Products " +
                        "where ID_Product not between 7 and 20 " +
                        "order by PackagesInBox, TradeName";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products,
                //	  для которых значение столбца ID_Product НЕ принадлежат диапазону от 7 до 20.
                //	  Отсортировать по убыванию в столбце Registration
                //	  При формировании запроса применять оператор ORDER ВУ ... DESC
                query = "select * " +
                        "from tbl_Products " +
                        "where ID_Product not between 7 and 20 " +
                        "order by Registration desc";
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
