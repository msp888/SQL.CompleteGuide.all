package task_15_05;

import task.utils.db.DBUtils;
import task.utils.db.TypeDBMS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_15_05 {


    public static void main(String[] args) throws IOException, SQLException {

        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
                    // отозвать ранее предоставленные роли у пользователей cat_user и mouse_user
                    String query =  "alter role r_full_access drop member cat_user";
                    stat.executeUpdate(query);
                    query =  "alter role r_read_only_access drop member mouse_user";
                    stat.executeUpdate(query);

                    // удалить ранее созданные роли
                    query =  "drop role r_full_access";
                    stat.executeUpdate(query);
                    query =  "drop role r_read_only_access";
                    stat.executeUpdate(query);

                    // удалить пользователей cat_user и mouse_user
                    query =  "drop user cat_user";
                    stat.executeUpdate(query);
                    query =  "drop user mouse_user";
                    stat.executeUpdate(query);

                    // удалить имена для входа на сервер cat и mouse
                    query =  "drop login cat";
                    stat.executeUpdate(query);
                    query =  "drop login mouse";
                    stat.executeUpdate(query);
                }
                else {
                    // отменить привилегии:
                    String query =  "REVOKE ALL ON PRODUCTS from \"cat\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON OFFICES from \"cat\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON SALESREPS from \"cat\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON CUSTOMERS from \"cat\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON ORDERS from \"cat\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON PRODUCTS from \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON OFFICES from \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON SALESREPS from \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON CUSTOMERS from \"mouse\"";
                    stat.executeUpdate(query);

                    query =  "REVOKE ALL ON ORDERS from \"mouse\"";
                    stat.executeUpdate(query);

                    // удалить пользователей cat и mouse
                    query =  "drop role \"cat\"";
                    stat.executeUpdate(query);
                    query =  "drop role \"mouse\"";
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
