package task_13_06;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_06 {

    public static void main(String[] args) throws IOException {

        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            conn.setAutoCommit(false);
            try {
                // удалить таблицы: ORDERS, SALESREPS, OFFICES, CUSTOMERS
                try (Statement stat = conn.createStatement())
                {
                    String query =  "ALTER TABLE SALESREPS " +
                                        "DROP CONSTRAINT SALESREPS_x1 ";
                    stat.executeUpdate(query);

                    query = "ALTER TABLE SALESREPS " +
                                "DROP CONSTRAINT SALESREPS_x2 ";
                    stat.executeUpdate(query);

                    query = "ALTER TABLE OFFICES " +
                                "DROP CONSTRAINT НASMGR ";
                    stat.executeUpdate(query);

                    query =  "drop table ORDERS ";
                    stat.executeUpdate(query);

                    query =  "drop table OFFICES ";
                    stat.executeUpdate(query);

                    query =  "drop table CUSTOMERS";
                    stat.executeUpdate(query);

                    query =  "drop table SALESREPS";
                    stat.executeUpdate(query);
                }

                // Подтверждение транзакции
                conn.commit();
            }
            catch (SQLException e)
            {
                conn.rollback();
                for (Throwable t : e)
                    System.out.println(t.getMessage());
            }
            finally {
                conn.setAutoCommit(true);
            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
