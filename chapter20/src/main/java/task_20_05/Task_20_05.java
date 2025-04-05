package task_20_05;

import task.utils.db.*;

import java.io.IOException;
import java.sql.*;

public class Task_20_05 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                try (Statement stat = conn.createStatement())
                {
                    // создать хранимую процедуру, которая по идентификатору сотрудника (входной параметр) возвращает через выходные параметры:
                    //		фамилию, имя, отчество, наименование должности и дату приема на работу
                    String query = "create procedure sp_EmployeeInfo " +
                            "@idEmployee int, " +
                            "@surname varchar(50) out, " +
                            "@name varchar(50) out, " +
                            "@middleName varchar(50) out, " +
                            "@position varchar(50) out, " +
                            "@employmentDate date out " +
                            "as " +
                            "begin " +
                            "select @surname = e.Surname, " +
                            "@name = e.Name, " +
                            "@middleName = e.MiddleName, " +
                            "@position = p.Name, " +
                            "@employmentDate = e.EmploymentDate " +
                            "from tbl_Employees e inner join tbl_Positions p " +
                            "on e.ID_Position = p.ID_Position " +
                            "where e.ID_Employee = @idEmployee " +
                            "end";
                    stat.executeUpdate(query);
                }

                // выполнить хранимую процедуру
                try ( CallableStatement cstmt = conn.prepareCall("{call dbo.sp_EmployeeInfo(?, ?, ?, ?, ?, ?)}") )
                {
                    int idEmployee = 2;
                    cstmt.setInt("idEmployee", idEmployee);
                    cstmt.registerOutParameter("surname", Types.VARCHAR);
                    cstmt.registerOutParameter("name", Types.VARCHAR);
                    cstmt.registerOutParameter("middleName", Types.VARCHAR);
                    cstmt.registerOutParameter("position", Types.VARCHAR);
                    cstmt.registerOutParameter("employmentDate", Types.DATE);
                    cstmt.execute();

                    System.out.printf("%s, %s, %s, %s, %s",
                            cstmt.getString("surname"),
                            cstmt.getString("name"),
                            cstmt.getString("middleName"),
                            cstmt.getString("position"),
                            cstmt.getDate("employmentDate").toString()
                    );
                }
            }
            else {
                try (Statement stat = conn.createStatement())
                {
                    // создать хранимую процедуру, которая по идентификатору сотрудника (входной параметр) возвращает через выходные параметры:
                    //		фамилию, имя, отчество, наименование должности и дату приема на работу
                    String query = "create or replace procedure sp_EmployeeInfo( " +
                            "in idEmployee int, " +
                            "out surname varchar(50), " +
                            "out name varchar(50), " +
                            "out middleName varchar(50), " +
                            "out aPosition varchar(50), " +
                            "out employmentDate date " +
                            ") " +
                            "language plpgsql " +
                            "as " +
                            "$$ " +
                            "begin " +
                            "select e.Surname, e.Name, e.MiddleName, p.Name, e.EmploymentDate " +
                            "into sp_EmployeeInfo.surname, sp_EmployeeInfo.name, sp_EmployeeInfo.middleName, " +
                            "sp_EmployeeInfo.aPosition, sp_EmployeeInfo.employmentDate " +
                            "from tbl_Employees e inner join tbl_Positions p " +
                            "on e.ID_Position = p.ID_Position " +
                            "where e.ID_Employee = idEmployee; " +
                            "end; " +
                            "$$;";

                    stat.executeUpdate(query);
                }

                // выполнить хранимую процедуру
                /*
                try ( CallableStatement cstmt = conn.prepareCall("{call sp_EmployeeInfo(?, ?, ?, ?, ?, ?)}") )
                {
                    int idEmployee = 2;
                    cstmt.setInt(1, idEmployee);
                    cstmt.registerOutParameter(2, Types.VARCHAR);
                    cstmt.registerOutParameter(3, Types.VARCHAR);
                    cstmt.registerOutParameter(4, Types.VARCHAR);
                    cstmt.registerOutParameter(5, Types.VARCHAR);
                    cstmt.registerOutParameter(6, Types.DATE);
                    cstmt.execute();

                    System.out.printf("%s, %s, %s, %s, %s",
                            cstmt.getString(2),
                            cstmt.getString(3),
                            cstmt.getString(4),
                            cstmt.getString(5),
                            cstmt.getDate(6).toString()
                    );
                }*/
                System.out.println("Внимание! Выполнить сохраненную процедуру через драйвер org.postgresql.jdbc, применяя CallableStatement, НЕВОЗМОЖНО!!!");
            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
