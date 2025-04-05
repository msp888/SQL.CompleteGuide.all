package task_7_04;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_7_04 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести список накладных (без содержания накладных) за 1.02.2024.
                //	  В результате должны быть: наименование зоны хранения, наименование состояния накладной, дата операции
                String query = "select st.Name as Area, os.Name as State, o.DateOfOperation " +
                               "from tbl_Orders o join tbl_StorageAreas st " +
                                    "on o.ID_StorageArea = st.ID_StorageArea " +
                                    "join tbl_OrderStates os " +
                                    "on o.ID_OrderState = os.ID_OrderState " +
                               "where o.DateOfOperation = '2024-02-19'";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести список накладных c содержанием накладных за 19.02.2024 для накладных с состоянием Начальные остатки.
                //	  В результате должны быть:
                //		наименование зоны хранения, наименование состояния накладной, дата операции,
                //		торговое наименование продукции, количество продукции, наименование единицы транспортировки,
                //		цена, дата производства, годен до, температура хранения, регистрационное удостоверение.
                //	  Данные отсортировать по идентификатору накладной и торговому наименованию продукции
                query = "select st.Name as Area, os.Name as State, o.DateOfOperation, p.TradeName, " +
                                "oi.Quantity, u.Name as Unit, oi.Price, oi.DateOfManufacture, oi.SellBy, oi.StorageTemperature, " +
                                "p.Certificate " +
                        "from tbl_Orders o join tbl_StorageAreas st " +
                            "on o.ID_StorageArea = st.ID_StorageArea " +
                            "join tbl_OrderStates os " +
                            "on o.ID_OrderState = os.ID_OrderState " +
                            "join tbl_OrderItems oi " +
                            "on o.ID_Order = oi.ID_Order " +
                            "join tbl_Products p " +
                            "on oi.ID_Product = p.ID_Product " +
                            "join tbl_TransportUnits u " +
                            "on oi.ID_TransportUnit = u.ID_TransportUnit " +
                        "where o.DateOfOperation = '2024-02-19' and os.ID_OrderState = 1 " +
                        "order by o.ID_Order, p.TradeName";
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
