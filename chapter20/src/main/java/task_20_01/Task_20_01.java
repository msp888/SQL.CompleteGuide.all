package task_20_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_20_01 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                int idOrder = 151;
                double m = 1.05;

                // вывести содержимое какой-то накладной
                System.out.println("До изменения цен: ");
                String query = String.format("select * " +
                        "from tbl_OrderItems " +
                        "where ID_Order = %d", idOrder);
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // создать хранимую процедуру, которая цену каждой позиции в указанной накладной умножаться на заданный вещественный коэффициент
                    query = "create procedure sp_multPriceInOrder @ID_Order int, @mult real " +
                            "as " +
                            "begin " +
                            "update tbl_OrderItems set Price = Price * @mult " +
                            "where ID_Order = @ID_Order " +
                            "end";
                    stat.executeUpdate(query);

                    // выполнить хранимую процедуру
                    query = String.format("exec sp_multPriceInOrder %d, %s ", idOrder, String.valueOf(m));
                    stat.executeUpdate(query);
                }
                else {
                    // создать хранимую процедуру, которая цену каждой позиции в указанной накладной умножаться на заданный вещественный коэффициент
                    query = "create procedure sp_multPriceInOrder(ID_Order int, mult real) " +
                            "language plpgsql " +
                            "as " +
                            "$$ " +
                            "begin " +
                            "update tbl_OrderItems as oi set Price = Price * mult " +
                            "where oi.ID_Order = sp_multPriceInOrder.ID_Order; " +
                            "end; " +
                            "$$;";
                    stat.executeUpdate(query);

                    // выполнить хранимую процедуру
                    query = String.format("call sp_multPriceInOrder(%d, %s)", idOrder, String.valueOf(m));
                    stat.executeUpdate(query);
                }

                // вывести измененные данные
                System.out.println("После изменения цен: ");
                query = String.format("select * " +
                                      "from tbl_OrderItems " +
                                      "where ID_Order = %d", idOrder);
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
