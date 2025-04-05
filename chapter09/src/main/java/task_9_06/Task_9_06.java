package task_9_06;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_9_06 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // в структуре wms_medicines есть ошибки. Одна из них: поля "Кол-во ПУ в коробке" и
                //	"Кол-во коробок на палете" расположены в таблице "Номенклатура" вместо таблицы
                //	"Единицы транспортировки".
                //	Из-за этого затруднены вычисления стоимости в одной строке содержимого накладной, т.к.
                //	количество может быть указано в палетах, коробках или ПУ, а цена в строке всегда указывается
                //	за одну ПУ.
                //	Используя соединение таблиц tbl_Orders, tbl_OrderItems, tbl_Products и опреатор CASE
                //	вывести идентификатор накладной, торговое наименование продукции, идентификатор единицы транспортировки,
                //	количество продукции (в указанной единице транспортировки) и
                //	количество продукции в пользовательских упаковках.
                //	Для вывода результата выбираем накладные от 2.03.2024.
                String query =  "select  o.ID_Order, p.TradeName, oi.ID_TransportUnit, oi.Quantity, " +
                                      "  case oi.ID_TransportUnit when 1 then oi.Quantity " +
                                                               "  when 2 then oi.Quantity * p.PackagesInBox " +
                                                               "  when 3 then oi.Quantity * p.PackagesInBox * p.BoxesOnPallet " +
                                                               "  else null  " +
                                      "  end as QuantityInCP " +
                                "from tbl_Orders o inner join tbl_OrderItems oi " +
                                    " on o.ID_Order = oi.ID_Order " +
                                  " inner join tbl_Products p " +
                                    " on oi.ID_Product = p.ID_Product " +
                                "where o.DateOfOperation = '2024-03-02'";
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
