package task_9_05;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_9_05 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // в запросе из задания task_9_03 применить оператор CAST для преобразования
                //	типа данных столбца суммарная стоимость всех выбранных накладных к типу money
                String query;
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    query = "select t.ID_Order, cast(round(sum(t.cost), 2) as money) as cost " +
                            "from (" +
                            "select o.ID_Order,  sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, " +
                            "count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            "  on o.ID_Order = oi.ID_Order " +
                            " join tbl_Products p " +
                            "  on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by o.ID_Order) t " +
                            "where t.rows > 6 " +
                            "group by t.ID_Order with rollup";
                }
                else {
                    query = "select t.ID_Order, round(cast(sum(t.cost) as numeric), 2) as cost " +
                            "from (" +
                            "select o.ID_Order,  sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, " +
                            "count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            "  on o.ID_Order = oi.ID_Order " +
                            " join tbl_Products p " +
                            "  on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by o.ID_Order) t " +
                            "where t.rows > 6 " +
                            "group by rollup(t.ID_Order) " +
                            "order by t.ID_Order";
                }
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
