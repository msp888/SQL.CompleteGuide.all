package task_15_02;

import task.utils.db.DBUtils;
import task.utils.db.TypeDBMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_15_02 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // создать роли:
                    //			Роль "Полный доступ": r_full_access
                    String query =  "create role r_full_access";
                    stat.executeUpdate(query);

                    //			Роль "Только чтение": r_read_only_access
                    query =  "create role r_read_only_access";
                    stat.executeUpdate(query);

                    // назначить ролям привилегии:
                    //			для r_full_access разрешить привилегии select, insert, update, delete для всех таблиц БД ed2
                    //			для r_read_only_access разрешить только select для всех таблиц БД ed2
                    query =  "GRANT select, insert, update, delete ON PRODUCTS to r_full_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON OFFICES to r_full_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON SALESREPS to r_full_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON CUSTOMERS to r_full_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON ORDERS to r_full_access";
                    stat.executeUpdate(query);


                    //			для r_read_only_access разрешить только select для всех таблиц БД ed2
                    query =  "GRANT select ON PRODUCTS to r_read_only_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON OFFICES to r_read_only_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON SALESREPS to r_read_only_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON CUSTOMERS to r_read_only_access";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON ORDERS to r_read_only_access";
                    stat.executeUpdate(query);


                    // предоставить роли пользователям:
                    //			пользователю cat_user предоставить роль r_full_access
                    //			пользователю mouse_user предоставить роль r_read_only_access
                    query =  "alter role r_full_access add member cat_user";
                    stat.executeUpdate(query);

                    //			пользователю mouse_user предоставить роль r_read_only_access
                    query =  "alter role r_read_only_access add member mouse_user";
                    stat.executeUpdate(query);
                }
                else {
                    // назначить ролям привилегии:
                    //			для r_full_access (cat) разрешить привилегии select, insert, update, delete для всех таблиц БД ed2
                    String query =  "GRANT select, insert, update, delete ON PRODUCTS to \"cat\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON OFFICES to \"cat\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON SALESREPS to \"cat\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON CUSTOMERS to \"cat\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select, insert, update, delete ON ORDERS to \"cat\"";
                    stat.executeUpdate(query);


                    //			для r_read_only_access (mouse) разрешить только select для всех таблиц БД ed2
                    query =  "GRANT select ON PRODUCTS to \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON OFFICES to \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON SALESREPS to \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON CUSTOMERS to \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "GRANT select ON ORDERS to \"mouse\"";
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
