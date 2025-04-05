package task_6_02;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_6_02 {

    public static void main(String[] args) throws IOException
    {
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                // вывести все строки таблицы tbl_Employees
                // вывести столбцы {Name, MiddleName, Surname} таблицы tbl_Employees
                String query = "select Name, MiddleName, Surname from tbl_Employees";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_Products, для которых значение столбца PackagesInBox равно 10
                // вывести столбцы {Certificate, TradeName} таблицы tbl_Products
                query = "select Certificate, TradeName from tbl_Products where PackagesInBox = 10";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                // вывести строки таблицы tbl_StorageAreaItems, для которых значение столбца ID_StorageAreaItem находится в диапазоне от 24 до 34.
                // вывести столбцы {ID_StorageAreaItem, ID_OrderItem, QuantityInCP} таблицы tbl_StorageAreaItems
                query = "select ID_StorageAreaItem, ID_OrderItem, QuantityInCP " +
                        "from tbl_StorageAreaItems " +
                        "where ID_StorageAreaItem >= 24 and ID_StorageAreaItem <= 34";
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
