package task_20_07;

import task.utils.db.*;

import java.io.IOException;
import java.sql.*;

public class Task_20_07 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            int idPosition = 0;

            if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                try (Statement stat = conn.createStatement())
                {
                    // создать хранимую процедуру, которая добавляет новую должность в БД и количество рабочих мест для этой должности.
                    //	Процедура должна возвращать идентификатор добавленной должности.
                    //	Если должность уже существует, то вернуть её идентификатор, а новую должность не добавлять.
                    String query = "create procedure sp_addPosition " +
                            "@namePosition varchar(50), " +
                            "@workplaces int, " +
                            "@id int out " +
                            "as " +
                            "begin " +
                            "  set @id = 0; " +
                            "  select @id = ID_Position " +
                            "  from tbl_Positions " +
                            "  where name = @namePosition " +
                            " " +
                            " if @id = 0 " +
                            "begin " +
                            "declare @localTableVar table( ID_Position int ) " +
                            "insert into tbl_Positions (Name, Workplaces) " +
                            " OUTPUT INSERTED.ID_Position INTO @localTableVar " +
                            "values (@namePosition, @workplaces) " +
                            " " +
                            "select @id = ID_Position " +
                            "from @localTableVar " +
                            "end " +
                            " " +
                            "return " +
                            "end";
                    stat.executeUpdate(query);
                }

                try ( CallableStatement cstmt = conn.prepareCall("{call dbo.sp_addPosition(?, ?, ?)}") )
                {
                    String posName = "Грузчик";
                    cstmt.setString("namePosition", posName);
                    cstmt.setInt("workplaces", 100);
                    cstmt.registerOutParameter("id", Types.INTEGER);
                    cstmt.execute();

                    System.out.printf("Идентификатор должности [%s] = %d%n", posName, cstmt.getInt("id"));
                }

                try ( CallableStatement cstmt = conn.prepareCall("{call dbo.sp_addPosition(?, ?, ?)}") )
                {
                    String posName = "Испытатель";
                    cstmt.setString("namePosition", posName);
                    cstmt.setInt("workplaces", 7);
                    cstmt.registerOutParameter("id", Types.INTEGER);
                    cstmt.execute();

                    idPosition = cstmt.getInt("id");
                    System.out.printf("Идентификатор должности [%s] = %d%n", posName, idPosition);
                }

                try (Statement stat = conn.createStatement())
                {
                    String query = "select * from tbl_Positions ";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    query = String.format("delete from tbl_Positions where ID_Position = %d", idPosition);
                    stat.executeUpdate(query);
                }
            }
            else {
                try (Statement stat = conn.createStatement())
                {
                    // создать хранимую процедуру, которая добавляет новую должность в БД и количество рабочих мест для этой должности.
                    //	Процедура должна возвращать идентификатор добавленной должности.
                    //	Если должность уже существует, то вернуть её идентификатор, а новую должность не добавлять.
                    String query = "create or replace procedure sp_addPosition(" +
                            "in namePosition varchar(50), " +
                            "in workplaces int, " +
                            "out id int " +
                            ") " +
                            "language plpgsql " +
                            "as " +
                            "$$ " +
                            "begin " +
                            "  select ID_Position into id " +
                            "  from tbl_Positions " +
                            "  where name = namePosition; " +
                            " " +
                            " if id is null then " +
                            "with tmp_table as ( " +
                            "insert into tbl_Positions (Name, Workplaces)  " +
                            "values (namePosition, workplaces)  " +
                            "RETURNING ID_Position " +
                            ") " +
                            "select ID_Position into id " +
                            "from tmp_table; " +
                            "end if;" +
                            " " +
                            "return; " +
                            "end; " +
                            "$$;";

                    stat.executeUpdate(query);
                }

                /*
                try ( CallableStatement cstmt = conn.prepareCall("{call dbo.sp_addPosition(?, ?, ?)}") )
                {
                    String posName = "Грузчик";
                    cstmt.setString("namePosition", posName);
                    cstmt.setInt("workplaces", 100);
                    cstmt.registerOutParameter("id", Types.INTEGER);
                    cstmt.execute();

                    System.out.printf("Идентификатор должности [%s] = %d%n", posName, cstmt.getInt("id"));
                }

                try ( CallableStatement cstmt = conn.prepareCall("{call dbo.sp_addPosition(?, ?, ?)}") )
                {
                    String posName = "Испытатель";
                    cstmt.setString("namePosition", posName);
                    cstmt.setInt("workplaces", 7);
                    cstmt.registerOutParameter("id", Types.INTEGER);
                    cstmt.execute();

                    idPosition = cstmt.getInt("id");
                    System.out.printf("Идентификатор должности [%s] = %d%n", posName, idPosition);
                }

                try (Statement stat = conn.createStatement())
                {
                    String query = "select * from tbl_Positions ";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    query = String.format("delete from tbl_Positions where ID_Position = %d", idPosition);
                    stat.executeUpdate(query);
                }
                 */
            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
