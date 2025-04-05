package task_20_03;

import task.utils.db.DBUtils;
import task.utils.db.TypeDBMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_20_03 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                int idEmployee = 2;

                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // создать хранимую функцию, которая возвращает наименование должности сотрудника
                    //		по идентификатору сотрудника
                    String query = "create function getEmployeePosition (@idEmployee int) " +
                            " returns varchar(50) " +
                            "begin " +
                            "  declare @res varchar(50); " +
                            "  select @res = p.Name " +
                            "  from tbl_Employees e inner join tbl_Positions p " +
                            "     on e.ID_Position = p.ID_Position " +
                            "  where e.ID_Employee = @idEmployee " +
                            "return @res " +
                            "end";
                    stat.executeUpdate(query);

                    // выполнить хранимую функцию
                    query = String.format("select dbo.getEmployeePosition(%d) as name ", idEmployee);
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }
                }
                else {
                    // создать хранимую функцию, которая возвращает наименование должности сотрудника
                    //		по идентификатору сотрудника
                    String query = "create function getEmployeePosition(idEmployee int) returns varchar(50) " +
                            "language plpgsql " +
                            "as " +
                            "$$ " +
                            "declare res varchar(50); " +
                            "begin " +
                            "  select p.Name into res " +
                            "  from tbl_Employees e inner join tbl_Positions p " +
                            "     on e.ID_Position = p.ID_Position " +
                            "  where e.ID_Employee = idEmployee; " +
                            "  return res; " +
                            "end; " +
                            "$$;";
                    stat.executeUpdate(query);

                    // выполнить хранимую функцию
                    query = String.format("select getEmployeePosition(%d) as name ", idEmployee);
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }
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
