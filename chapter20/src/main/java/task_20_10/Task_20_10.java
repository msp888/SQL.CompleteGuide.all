package task_20_10;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_20_10 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // удалить триггер, созданный в задании task_20_09
                    String query = "drop trigger tr_ud_tbl_Employees ";
                    stat.executeUpdate(query);

                    // удалить таблицу tbl_EmployeesBackup
                    query = "drop table tbl_EmployeesBackup ";
                    stat.executeUpdate(query);
                }
                else {
                    // удалить триггер, созданный в задании task_20_09
                    String query = "drop trigger tr_ud_Employees on tbl_Employees ";
                    stat.executeUpdate(query);

                    // удалить триггерную функцию, созданную в задании task_20_09
                    query = "drop function trg_employees ";
                    stat.executeUpdate(query);

                    // удалить таблицу tbl_EmployeesBackup
                    query = "drop table tbl_EmployeesBackup ";
                    stat.executeUpdate(query);
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
