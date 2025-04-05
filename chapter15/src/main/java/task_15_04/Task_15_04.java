package task_15_04;

import task.utils.db.DBUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_15_04 {


    public static void main(String[] args) throws IOException, SQLException {

        // подключиться к ed2 от имени пользователя mouse и выполнить пару запросов к БД,
        //		показывающих, что доступ с какими-то привилегиями к таблицам есть, а с какими-то привилегиями - доступ ограничен.
        try (Connection conn = DBUtils.getConnectionWithLogin("ed2", "mouse", "mouse"))
        {
            try (Statement stat = conn.createStatement())
            {
                System.out.println("Выбор данных из таблицы OFFICES:");
                String query =  "select * from OFFICES";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    DBUtils.printTable(rs);
                }

                System.out.println("Удаление данных из таблицы ORDERS:");
                try
                {
                    query =  "delete from ORDERS";
                    stat.executeUpdate(query);
                    System.out.println(" -- успешно");
                }
                catch (SQLException e)
                {
                    System.out.println(" -- ошибка");
                    for (Throwable t : e)
                        System.out.println(t.getMessage());
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
