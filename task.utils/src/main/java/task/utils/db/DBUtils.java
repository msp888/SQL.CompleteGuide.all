package task.utils.db;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DBUtils {

    /**
     * Получить тип СУБД
     * */
    public static TypeDBMS getTypeDBMS() {
        Properties props = new Properties();
        try (InputStream in = getStreamProperties()) {
            props.load(in);
        } catch (IOException e) {
            return TypeDBMS.UNKNOWN;
        }

        if ( props.getProperty("jdbc.drivers").equals("com.microsoft.sqlserver.jdbc.SQLServerDriver") ) {
            return TypeDBMS.MSSQL;
        }

        if ( props.getProperty("jdbc.drivers").equals("org.postgresql.Driver") ) {
            return TypeDBMS.PostgreSQL;
        }

        return TypeDBMS.UNKNOWN;
    }

    /**
     * Получить поток для доступа к файлу свойств через ресурсы
     * */
    private static InputStream getStreamProperties() throws IOException
    {
        try {
            Class<?> cl = Class.forName("task.utils.db.DBUtils");
            String rname = "/db.properties";
            return cl.getResourceAsStream(rname);

        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }

    /**
     * Получить подключение к заданной БД
     * */
    public static Connection getConnection(String db_name) throws SQLException, IOException
    {
        Properties props = new Properties();
        try ( InputStream in = getStreamProperties() )
        {
            props.load(in);
        }

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) {
            System.setProperty("jdbc.drivers", drivers);
        }

        String url = props.getProperty("jdbc.url") + db_name;
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Получить подключение к БД по умолчанию: wms_medicines
     * */
    public static Connection getConnection() throws SQLException, IOException
    {
        return getConnection("wms_medicines");
    }

    /**
     * Получить подключение к заданной БД с указанием логина и пароля
     * */
    public static Connection getConnectionWithLogin(String db_name, String user, String password) throws SQLException, IOException
    {
        var props = new Properties();
        try ( InputStream in = getStreamProperties() )
        {
            props.load(in);
        }

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);

        String url = props.getProperty("jdbc.url") + db_name;

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Получить подключение к СУБД (без указания БД)
     * */
    public static Connection getConnectionDBMS() throws SQLException, IOException
    {
        var props = new Properties();
        try ( InputStream in = getStreamProperties() )
        {
            props.load(in);
        }

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);

        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Печать таблицы: результата запроса
     * */
    public static void printTable(ResultSet rs) throws SQLException, IOException
    {
        // Получаем метаданные запроса
        ResultSetMetaData rsmd = rs.getMetaData();

        // Определяем ширину столбцов
        ArrayList<Integer> l = new ArrayList<>();
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            l.add(
                    Math.min(Math.max(rsmd.getColumnDisplaySize(i),
                                    rsmd.getColumnLabel(i).length())
                            ,50)
            );
        }

        // Печать наименований столбцов
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            System.out.print(String.format("%-"+l.get(i-1)+"s", rsmd.getColumnLabel(i)) + "\t");
        }
        System.out.println();

        // Печать разделителя между наименованиями столбцов и данными столбцов
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            System.out.print("-".repeat(l.get(i-1)) + "\t");
        }
        System.out.println();

        int numberOfRecords = 0; // Количество строк в результирующем наборе данных

        // Печать данных столбцов
        while (rs.next()) {
            numberOfRecords++;
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String value = rs.getString(i);
                if (value == null)
                    value = "Null";
                String subStr = value.length() > l.get(i-1) ? value.substring(0, l.get(i-1)) : value;
                System.out.print(String.format("%-"+l.get(i-1)+"s", subStr) + "\t");
            }
            System.out.println();
        }
        System.out.println(">>> количество строк: "+numberOfRecords+" <<<");
        System.out.println();
    }

}
