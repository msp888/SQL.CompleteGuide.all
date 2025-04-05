package task_14_02;

import task.utils.db.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_14_02 {


    public static void main(String[] args) throws IOException, SQLException {

        // из запроса, разработанного в task_9_06 создать представление
        //		Выбор (where) по заданной дате из запроса убрать, а столбец DateOfOperation добавить в результат запроса (select)
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                String query =  "create view view_QuantityInUserPack as " +
                                    "select  o.ID_Order, p.TradeName, oi.ID_TransportUnit, oi.Quantity, " +
                                                "  case oi.ID_TransportUnit " +
                                                    "  when 1 then oi.Quantity " +
                                                    "  when 2 then oi.Quantity * p.PackagesInBox " +
                                                    "  when 3 then oi.Quantity * p.PackagesInBox * p.BoxesOnPallet " +
                                                    "  else null  " +
                                                "  end as QuantityInCP, " +
                                                " o.DateOfOperation " +
                                    "from tbl_Orders o inner join tbl_OrderItems oi " +
                                                " on o.ID_Order = oi.ID_Order " +
                                            " inner join tbl_Products p " +
                                                " on oi.ID_Product = p.ID_Product ";
                stat.executeUpdate(query);
            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
