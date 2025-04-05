package task_20_09;

import task.utils.db.*;

import java.io.IOException;
import java.sql.*;

public class Task_20_09 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection())
        {
            if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                try (Statement stat = conn.createStatement())
                {
                    // создать триггер для таблицы tbl_Employees, который выполняется по обновлению строк или удалению строк из таблицы.
                    //	Триггер должен проверять: существует ли таблица tbl_EmployeesBackup. Если таблица не существует,
                    //	то триггер создает эту таблицу.
                    // Первые семь столбцов аналогичны столбцам tbl_Employees.
                    //	При изменениях (обновление, удаление) таблицы tbl_Employees измененные строки должны заноситься в таблицу
                    //	tbl_EmployeesBackup. При этом в столбец DateOfChange заносится дата и время изменений,
                    //	а в столбец TypeOfOperation - тип операции: 1-update; 2-delete.
                    String query =  "create trigger tr_ud_tbl_Employees on tbl_Employees " +
                            "for update, delete " +
                            "as " +
                            "begin " +
                            "if OBJECT_ID('tbl_EmployeesBackup', 'U') IS NULL " +
                            "begin " +
                            "CREATE TABLE tbl_EmployeesBackup " +
                            "( " +
                            "ID_Employee int, " +
                            "ID_Position int, " +
                            "Surname varchar(50), " +
                            "Name varchar(50), " +
                            "MiddleName varchar(50), " +
                            "DateOfBirth date, " +
                            "EmploymentDate date, " +
                            "DateOfChange datetime, " +
                            "TypeOfOperation int " +
                            ") " +
                            "end " +
                            " " +
                            "declare @TypeOfOperation int = 2; /* по умолчанию - удаление */ " +
                            "if (select count(*) from inserted) > 0 " +
                            "set @TypeOfOperation = 1; /* обновление */ " +
                            " " +
                            "insert into tbl_EmployeesBackup (ID_Employee, ID_Position, Surname, Name, MiddleName, DateOfBirth, " +
                            "EmploymentDate, DateOfChange, TypeOfOperation) " +
                            "select ID_Employee, ID_Position, Surname, Name, MiddleName, DateOfBirth, " +
                            "EmploymentDate, GETDATE(), @TypeOfOperation " +
                            "from deleted " +
                            "end";
                    stat.executeUpdate(query);
                }
            }
            else {
                try (Statement stat = conn.createStatement())
                {
                    // создать триггер для таблицы tbl_Employees, который выполняется по обновлению строк или удалению строк из таблицы.
                    //	Триггер должен проверять: существует ли таблица tbl_EmployeesBackup. Если таблица не существует,
                    //	то триггер создает эту таблицу.
                    // Первые семь столбцов аналогичны столбцам tbl_Employees.
                    //	При изменениях (обновление, удаление) таблицы tbl_Employees измененные строки должны заноситься в таблицу
                    //	tbl_EmployeesBackup. При этом в столбец DateOfChange заносится дата и время изменений,
                    //	а в столбец TypeOfOperation - тип операции: 1-update; 2-delete.

                    // Создаем триггерную функцию
                    String query =  "create or replace function trg_Employees() returns trigger " +
                            "language plpgsql " +
                            "as " +
                            "$$ " +
                            "declare is_exists int; " +
                            "TypeOfOperation int = 2; /* по умолчанию - удаление */" +
                            "begin " +
                                "select count(*) into is_exists " +
                                "from information_schema.tables " +
                                "where table_name = 'tbl_employeesbackup' and " +
                                "table_schema = 'public';" +
                                " " +
                                " RAISE NOTICE 'is_exists = %', is_exists; " +
                                " " +
                                "if is_exists = 0 then " +
                                    "CREATE TABLE tbl_EmployeesBackup " +
                                    "( " +
                                    "ID_Employee int, " +
                                    "ID_Position int, " +
                                    "Surname varchar(50), " +
                                    "Name varchar(50), " +
                                    "MiddleName varchar(50), " +
                                    "DateOfBirth date, " +
                                    "EmploymentDate date, " +
                                    "DateOfChange timestamptz, " +
                                    "TypeOfOperation int " +
                                    "); " +
                                "end if; " +
                                " " +
                                "if (TG_OP = 'UPDATE') then " +
                                    "TypeOfOperation:=1; /* обновление */ " +
                                "end if; " +
                                " " +
                                "insert into tbl_EmployeesBackup (ID_Employee, ID_Position, Surname, Name, MiddleName, DateOfBirth, " +
                                "EmploymentDate, DateOfChange, TypeOfOperation) " +
                                "values(old.ID_Employee, old.ID_Position, old.Surname, old.Name, old.MiddleName, old.DateOfBirth, " +
                                "old.EmploymentDate, now(), TypeOfOperation); " +
                                " " +
                                "return null;" +
                            "end; " +
                            "$$;";
                    stat.executeUpdate(query);

                    // Создаем триггер
                    query = "create or replace trigger tr_ud_Employees after update or delete on tbl_Employees " +
                            "for each row execute function trg_Employees();";
                    stat.executeUpdate(query);

                }
            }

            // выполнить различные изменения в таблице tbl_Employees
            try (Statement stat = conn.createStatement())
            {
                String tmp = "##test##";

                String query = String.format("insert into tbl_Employees " +
                                "(Surname, Name, MiddleName, DateOfBirth, EmploymentDate, ID_Position) " +
                                "values ('%s', '%s', '%s', '2000-01-01', '2024-01-01', 1)",
                                tmp, tmp, tmp);
                stat.executeUpdate(query);

                query =  "update tbl_Employees set Surname = 'Новая', Name = 'Еще', MiddleName = 'Новее' " +
                                "where ID_Employee = 3";
                stat.executeUpdate(query);

                query = "delete from tbl_Employees where ID_Employee in (3, 4)";
                stat.executeUpdate(query);

                query = String.format("delete from tbl_Employees where Surname = '%s'", tmp);
                stat.executeUpdate(query);
            }

            // вывести содержимое таблиц tbl_Employees и tbl_EmployeesBackup
            try (Statement stat = conn.createStatement())
            {
                String query = "select * from tbl_Employees ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                query = "select * from tbl_EmployeesBackup ";
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
