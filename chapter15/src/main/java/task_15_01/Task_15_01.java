package task_15_01;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_15_01 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // создать имена для входа на сервер:
                    //			логин: cat
                    //			пароль: cat
                    String query =  "create login cat with password = 'cat'";
                    stat.executeUpdate(query);

                    //			логин: mouse
                    //			пароль: mouse
                    query =  "create login mouse with password = 'mouse'";
                    stat.executeUpdate(query);

                    // создать пользователей базы данных ed2:
                    //			cat_user с логином cat
                    query =  "create user cat_user for login cat";
                    stat.executeUpdate(query);

                    //			mouse_user с логином mouse
                    query =  "create user mouse_user for login mouse";
                    stat.executeUpdate(query);
                }
                else {
                    // создать пользователей базы данных ed2:
                    //			логин: cat
                    //			пароль: cat
                    String query =  "create role \"cat\" login password 'cat'";
                    stat.executeUpdate(query);

                    //			логин: mouse
                    //			пароль: mouse
                    query =  "create role \"mouse\" login password 'mouse'";
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
