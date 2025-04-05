package task_12_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task_12_01 {

    public static void main(String[] args) throws IOException {

        // в рамках одной транзакции сформировать требуемое количество запросов для
        //		добавления новой должности и назначение нового сотрудника на должность.
        //		Проверить, что после завершения транзакции сотрудник назначен на должность:
        //		вывести ФИО сотрудника и его должность;
        //      удалить добавленного сотрудника, удалить добавленную должность

        try (Connection conn = DBUtils.getConnection())
        {
            int idPosition = 0;
            int idEmployee = 0;

            conn.setAutoCommit(false); // Запрет автоматического подтверждения транзакций
            try {
                try (Statement stat = conn.createStatement())
                {
                    String query = String.format("insert into tbl_Positions (Name, Workplaces) " +
                            "values ('%s', %d)", "Бухгалтер", 10);
                    if (stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                        try (ResultSet rs = stat.getGeneratedKeys())
                        {
                            if (rs.next()) {
                                idPosition = rs.getInt(1);
                            }
                        }
                    }

                    String surname = "Юг";
                    String name = "Андрей";
                    String middleName = "Андреевич";
                    Calendar dateOfBirth = new GregorianCalendar(1980, Calendar.JANUARY, 1);
                    Calendar employmentDate = new GregorianCalendar(2024, Calendar.OCTOBER, 2);

                    SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

                    query = String.format("insert into tbl_Employees " +
                                          "(Surname, Name, MiddleName, DateOfBirth, EmploymentDate, ID_Position) " +
                                          "values ('%s', '%s', '%s', '%s', '%s', %d)",
                            surname,
                            name,
                            middleName,
                            formater.format(dateOfBirth.getTime()),
                            formater.format(employmentDate.getTime()),
                            idPosition);

                    if (stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                        try (ResultSet rs = stat.getGeneratedKeys())
                        {
                            if (rs.next()) {
                                idEmployee = rs.getInt(1);
                            }
                        }
                    }
                }

                // Подтверждение транзакции
                conn.commit();
            }
            catch (SQLException e)
            {
                // Тут обрабатываем исключения SQLException, которые формируются при выполнении запросов:

                // - отменяет транзакцию, т.к. обнаружена ошибка
                conn.rollback();

                // - выводим информацию об ошибке
                for (Throwable t : e)
                    System.out.println(t.getMessage());
            }
            finally {
                conn.setAutoCommit(true);   // Разрешение автоматического подтверждения транзакций, на случай,
                                            // если в рамках этого соединения будут еще запросы, но транзакции
                                            // для них должны подтверждаться автоматически
            }

            // вывести ФИО сотрудника и его должность;
            try (Statement stat = conn.createStatement())
            {
                String query =  "select e.Surname, e.Name, e.MiddleName, p.Name as Position " +
                        "from tbl_Employees e inner join tbl_Positions p " +
                        "on e.ID_Position = p.ID_Position " +
                        "where ID_Employee = " + idEmployee;

                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }
            }

            // удалить добавленного сотрудника, удалить добавленную должность
            try (Statement stat = conn.createStatement())
            {
                String query = String.format("delete from tbl_Employees " +
                        "where ID_Employee = %d", idEmployee);
                stat.executeUpdate(query);

                query = String.format("delete from tbl_Positions " +
                        "where ID_Position = %d", idPosition);
                stat.executeUpdate(query);
            }

        }
        catch (SQLException e)
        {
            // Тут обрабатываем все необработанные исключения SQLException
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }

    }

}
