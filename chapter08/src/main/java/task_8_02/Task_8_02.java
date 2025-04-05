package task_8_02;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_8_02 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // на основании накладных с состоянием "Начальные остатки" сформировать отчет:
                //	для каждого наименования продукции вывести торговое наименование, минимальную и максимальную цену
                //	для этой продукции, минимальную и максимальную дату производства для этой продукции,
                //	количество строк в каждой группе.
                //	Результат отсортировать по торговому наименованию
                String query =  "select p.TradeName, min(oi.Price) as minPrice, max(oi.Price) as maxPrice, " +
                                "min(oi.DateOfManufacture) as minDate, max(oi.DateOfManufacture) as maxDate, " +
                                "count(p.TradeName) as rows " +
                                "from tbl_Orders o inner join tbl_OrderItems oi " +
                                "  on o.ID_Order = oi.ID_Order " +
                                "join tbl_Products p " +
                                "  on oi.ID_Product = p.ID_Product " +
                                "where o.ID_OrderState = 1 " +
                                "group by p.TradeName " +
                                "order by p.TradeName";
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
