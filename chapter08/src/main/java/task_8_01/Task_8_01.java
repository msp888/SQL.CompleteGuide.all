package task_8_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_8_01 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // определить количество строк в таблице Номенклатура.
                //	Имя столбца в рехультирующем запросе должно присутствовать
                String query = "select count(*) as rows " +
                               "from tbl_Products";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // для накладных с состоянием "Начальные остатки" от 19.02.2024 вывести идентификатор накладной,
                //	стоимость по накладной и количество строк в каждой группе.
                //	Стоимость по накладной - это сумма стоимостей товара из каждой строки накладной.
                //	При вычислении стоимости товара в каждой строке накладной нужно учитывать:
                //		1) в накладных с состоянием "Начальные остатки" количество в каждой строке накладной
                //		указывается в палетах, а цена в строке накладной указана за пользовательскую упаковку (ПУ).
                //		Таким образом, чтобы вычислить стоимости товара в строке накладной, нужно сначала посчитать
                //		сколько ПУ находится на одной палете;
                //		2) количество ПУ в одной коробке и количество коробок на палете можно получить из таблицы Номенклатура.
                query = "select o.ID_Order, sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, count(o.ID_Order) as rows " +
                        "from tbl_Orders o inner join tbl_OrderItems oi " +
                        " on o.ID_Order = oi.ID_Order " +
                        "join tbl_Products p " +
                        " on oi.ID_Product = p.ID_Product " +
                        "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                        "group by o.ID_Order";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // к предыдущему запросу добавить вывод суммарной стоимости всех выбранных накладных
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    query = "select o.ID_Order, sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            " on o.ID_Order = oi.ID_Order " +
                            "join tbl_Products p " +
                            " on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by o.ID_Order with rollup";
                }
                else {
                    query = "select o.ID_Order, sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            " on o.ID_Order = oi.ID_Order " +
                            "join tbl_Products p " +
                            " on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by rollup(o.ID_Order) " +
                            "order by o.ID_Order";
                }
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // доработать предыдущий запрос так, чтобы в расчетах учитывались только накладные,
                //	в которых больше 6 строк.
                //	Объяснить: почему суммарная стоимость всех выбранных накладных получилась неправильная?
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    query = "select o.ID_Order, sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            " on o.ID_Order = oi.ID_Order " +
                            "join tbl_Products p " +
                            " on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by o.ID_Order with rollup " +
                            "having count(oi.ID_OrderItem) > 6";
                }
                else {
                    query = "select o.ID_Order, sum(oi.Quantity * p.PackagesInBox * p.BoxesOnPallet * oi.Price) as cost, count(o.ID_Order) as rows " +
                            "from tbl_Orders o inner join tbl_OrderItems oi " +
                            " on o.ID_Order = oi.ID_Order " +
                            "join tbl_Products p " +
                            " on oi.ID_Product = p.ID_Product " +
                            "where o.DateOfOperation = '2024-02-19' and o.ID_OrderState = 1 " +
                            "group by rollup(o.ID_Order) " +
                            "having count(oi.ID_OrderItem) > 6 " +
                            "order by o.ID_Order";
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
