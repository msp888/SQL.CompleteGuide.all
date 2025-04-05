package task_12_02;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Task_12_02 {

    public static void main(String[] args) throws IOException {

        // вывести количество строк в таблицах: tbl_Positions, tbl_Employees (не через функцию DBUtils.printTable()).
        //		Начать новую транзакцию, в рамках которой:
        //		сформировать требуемое количество запросов для добавления новой должности и
        //		назначение нового сотрудника на должность (как в предыдущем задании).
        //		Удалить все записи из таблицы tbl_Positions.
        //      Подтвердить транзакцию.
        //		После завершения транзакции убедиться, что количество строк в таблицах:
        //		tbl_Positions, tbl_Employees не изменилось.	Объяснить почему...

        try (Connection conn = DBUtils.getConnection())
        {
            int idPosition = 0;
            int idEmployee = 0;
            int countPositions = 0;
            int countEmployees = 0;

            // вывести количество строк в таблицах: tbl_Positions, tbl_Employees
            try (Statement stat = conn.createStatement())
            {
                String query =  "select count(*) as rows " +
                                "from tbl_Positions ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    if (rs.next()) {
                        countPositions = rs.getInt("rows");
                    }
                }

                query = "select count(*) as rows " +
                        "from tbl_Employees ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    if (rs.next()) {
                        countEmployees = rs.getInt("rows");
                    }
                }

                System.out.println("(перед) Количество строк в tbl_Positions: " + countPositions);
                System.out.println("(перед) Количество строк в tbl_Employees: " + countEmployees);
            }

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

                // Удалить все записи из таблицы tbl_Positions
                try (Statement stat = conn.createStatement())
                {
                    String query = "delete from tbl_Positions";
                    stat.executeUpdate(query);
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

            countPositions = 0;
            countEmployees = 0;

            // вывести количество строк в таблицах: tbl_Positions, tbl_Employees
            try (Statement stat = conn.createStatement())
            {
                String query =  "select count(*) as rows " +
                        "from tbl_Positions ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    if (rs.next()) {
                        countPositions = rs.getInt("rows");
                    }
                }

                query = "select count(*) as rows " +
                        "from tbl_Employees ";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    if (rs.next()) {
                        countEmployees = rs.getInt("rows");
                    }
                }

                System.out.println("(после) Количество строк в tbl_Positions: " + countPositions);
                System.out.println("(после) Количество строк в tbl_Employees: " + countEmployees);
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
