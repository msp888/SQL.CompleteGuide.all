package task_6_03;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_03 {

    public static void main(String[] args) throws IOException
    {
        System.out.println("Изучаем Git...");
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести столбцы {ID_StorageAreaItem, Location, QuantityInCP, (Location + QuantityInCP)} таблицы tbl_StorageAreaItems,
                // для которых значение столбца ID_StorageAreaItem находится в диапазоне от 24 до 34.
                String query = "select ID_StorageAreaItem, Location, QuantityInCP, (Location + QuantityInCP) " +
                                "from tbl_StorageAreaItems " +
                                "where ID_StorageAreaItem >= 24 and ID_StorageAreaItem <= 34";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести столбцы {ID_StorageAreaItem, Location, QuantityInCP, (Location + QuantityInCP)} таблицы tbl_StorageAreaItems,
                // для которых значение столбца ID_StorageAreaItem находится в диапазоне от 24 до 34 и
                //	  (Location + QuantityInCP) > 40.
                query = "select ID_StorageAreaItem, Location, QuantityInCP, (Location + QuantityInCP) " +
                        "from tbl_StorageAreaItems " +
                        "where ID_StorageAreaItem >= 24 and ID_StorageAreaItem <= 34 " +
                        " and (Location + QuantityInCP) > 40";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // повторить предыдущий запрос так, но отобрать только строки где (Location + QuantityInCP) равно Null.
                //	  Применить IS NULL
                query = "select ID_StorageAreaItem, Location, QuantityInCP, (Location + QuantityInCP) " +
                        "from tbl_StorageAreaItems " +
                        "where ID_StorageAreaItem >= 24 and ID_StorageAreaItem <= 34 " +
                        " and (Location + QuantityInCP) is null";
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
