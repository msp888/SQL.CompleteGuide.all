package task_7_05;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_7_05 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести список накладных c содержанием накладных за 2.03.2024.
                //	  В результате должны быть:
                //		дата операции, ID_OrderState, количество продукции, цена, Количество в пользовательских упаковках.
                //	  В сформированном запросе не должно быть секции where.
                //	  Подсказка: Результат запроса содержит 5 строк.
                String query = "select o.DateOfOperation, o.ID_OrderState, oi.Quantity, oi.Price, sai.QuantityInCP " +
                        "from tbl_Orders o inner join tbl_OrderItems oi " +
                            "on o.ID_Order = oi.ID_Order and o.DateOfOperation = '2024-03-02' " +
                            "left outer join tbl_StorageAreaItems sai " +
                            "on oi.ID_OrderItem = sai.ID_OrderItem ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                //	  тоже самое, только с применением синтаксиса переходов для внешних соединений
                query = "select o.DateOfOperation, o.ID_OrderState, oi.Quantity, oi.Price, sai.QuantityInCP " +
                        "from {oj tbl_Orders o inner join tbl_OrderItems oi " +
                        "on o.ID_Order = oi.ID_Order and o.DateOfOperation = '2024-03-02' " +
                        "left outer join tbl_StorageAreaItems sai " +
                        "on oi.ID_OrderItem = sai.ID_OrderItem} ";
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
