package task_13_03;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_03 {

    public static void main(String[] args) throws IOException {

        if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
            try (Connection conn = DBUtils.getConnectionDBMS())
            {
                conn.setAutoCommit(false);
                try {
                    // заполнить таблицы БД ed2, выбрав данные из аналогичных таблиц БД educational.
                    //		По одному запросу на каждую таблицу... за исключением столбцов OFFICES.MGR и SALESREPS.МANAGER.
                    //		Их сначала заполняем значение Null, а затем, после заполнения все таблиц, нужно добавить
                    //		два запроса, которые заполнят и эти поля правильными значениями
                    try (Statement stat = conn.createStatement())
                    {
                        String query =  "insert into ed2..PRODUCTS(MFR_ID, PRODUCT_ID, DESCRIPTION, PRICE, QTY_ON_HAND) " +
                                "select MFR_ID, PRODUCT_ID, DESCRIPTION, PRICE, QTY_ON_HAND " +
                                "from educational..PRODUCTS";
                        stat.executeUpdate(query);

                        query = "insert into ed2..OFFICES(OFFICE, CITY, REGION, MGR, TARGET, SALES) " +
                                "select OFFICE, CITY, REGION, null, TARGET, SALES " +
                                "from educational..OFFICES";
                        stat.executeUpdate(query);

                        query = "insert into ed2..SALESREPS(EMPL_NUМ, NAME, AGE, REP_OFFICE, TITLE, HIRE_DATE, МANAGER, QUOTA, SALES) " +
                                "select EMPL_NUМ, NAME, AGE, REP_OFFICE, TITLE, HIRE_DATE, null, QUOTA, SALES " +
                                "from educational..SALESREPS";
                        stat.executeUpdate(query);

                        query = "insert into ed2..CUSTOMERS(CUST_NUМ, COMPANY, CUST_REP, CREDIT_LIMIT) " +
                                "select CUST_NUМ, COMPANY, CUST_REP, CREDIT_LIMIT " +
                                "from educational..CUSTOMERS";
                        stat.executeUpdate(query);

                        query = "insert into ed2..ORDERS(ORDER_NUМ, ORDER_DATE, CUST, REP, MFR, PRODUCT, QTY, AМOUNT) " +
                                "select ORDER_NUМ, ORDER_DATE, CUST, REP, MFR, PRODUCT, QTY, AМOUNT " +
                                "from educational..ORDERS";
                        stat.executeUpdate(query);

                        query = "update ed2..OFFICES set ed2..OFFICES.MGR = " +
                                "(select t.MGR from educational..OFFICES t where ed2..OFFICES.OFFICE = t.OFFICE) ";
                        stat.executeUpdate(query);

                        query = "update ed2..SALESREPS set ed2..SALESREPS.МANAGER = " +
                                "(select t.МANAGER from educational..SALESREPS t where ed2..SALESREPS.EMPL_NUМ = t.EMPL_NUМ) ";
                        stat.executeUpdate(query);
                    }

                    // Подтверждение транзакции
                    conn.commit();
                }
                catch (SQLException e)
                {
                    conn.rollback();
                    for (Throwable t : e)
                        System.out.println(t.getMessage());
                }
                finally {
                    conn.setAutoCommit(true);
                }

                // вывести содержимое таблиц БД ed2
                try (Statement stat = conn.createStatement())
                {
                    // PRODUCTS
                    String query = "select * from ed2..PRODUCTS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // OFFICES
                    query = "select * from ed2..OFFICES";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // SALESREPS
                    query = "select * from ed2..SALESREPS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // CUSTOMERS
                    query = "select * from ed2..CUSTOMERS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // ORDERS
                    query = "select * from ed2..ORDERS";
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
        else {
            // Подключаемся именно к ed2
            try (Connection conn = DBUtils.getConnection("ed2"))
            {
                // Установить расширение postgres_fdw
                try (Statement stat = conn.createStatement())
                {
                    String query =  "create extension if not exists postgres_fdw";
                    stat.executeUpdate(query);
                }

                // Создать сторонний сервер
                try (Statement stat = conn.createStatement())
                {
                    String query =  "create server if not exists educational_server " +
                            "        foreign data wrapper postgres_fdw " +
                            "        options (dbname 'educational') ";
                    stat.executeUpdate(query);
                }

                // Создать сопоставление пользователей
                try (Statement stat = conn.createStatement())
                {
                    String query =  "create user mapping if not exists for postgres " +
                            "        server educational_server " +
                            "        options (user 'postgres', password '12') ";
                    stat.executeUpdate(query);
                }

                // Создать дополнительную схему для хранения ссылок на удаленные таблицы
                try (Statement stat = conn.createStatement())
                {
                    String query =  "create schema if not exists ext";
                    stat.executeUpdate(query);
                }

                // Импорт определений таблиц из удалённой схемы
                try (Statement stat = conn.createStatement())
                {
                    String query =  "import foreign schema public " +
                            "    from server educational_server into ext";
                    stat.executeUpdate(query);
                }

                // Приступаем к копированию данных: копируем из одной схемы в другую в пределах одной БД
                conn.setAutoCommit(false);
                try {
                    try (Statement stat = conn.createStatement())
                    {
                        String query =  "insert into public.PRODUCTS(MFR_ID, PRODUCT_ID, DESCRIPTION, PRICE, QTY_ON_HAND) " +
                                "select MFR_ID, PRODUCT_ID, DESCRIPTION, PRICE, QTY_ON_HAND " +
                                "from ext.PRODUCTS";
                        stat.executeUpdate(query);

                        query = "insert into public.OFFICES(OFFICE, CITY, REGION, MGR, TARGET, SALES) " +
                                "select OFFICE, CITY, REGION, null, TARGET, SALES " +
                                "from ext.OFFICES";
                        stat.executeUpdate(query);

                        query = "insert into public.SALESREPS(EMPL_NUМ, NAME, AGE, REP_OFFICE, TITLE, HIRE_DATE, МANAGER, QUOTA, SALES) " +
                                "select EMPL_NUМ, NAME, AGE, REP_OFFICE, TITLE, HIRE_DATE, null, QUOTA, SALES " +
                                "from ext.SALESREPS";
                        stat.executeUpdate(query);

                        query = "insert into public.CUSTOMERS(CUST_NUМ, COMPANY, CUST_REP, CREDIT_LIMIT) " +
                                "select CUST_NUМ, COMPANY, CUST_REP, CREDIT_LIMIT " +
                                "from ext.CUSTOMERS";
                        stat.executeUpdate(query);

                        query = "insert into public.ORDERS(ORDER_NUМ, ORDER_DATE, CUST, REP, MFR, PRODUCT, QTY, AМOUNT) " +
                                "select ORDER_NUМ, ORDER_DATE, CUST, REP, MFR, PRODUCT, QTY, AМOUNT " +
                                "from ext.ORDERS";
                        stat.executeUpdate(query);

                        query = "update public.OFFICES as o set MGR = " +
                                "(select t.MGR from ext.OFFICES t where o.OFFICE = t.OFFICE) ";
                        stat.executeUpdate(query);

                        query = "update public.SALESREPS as s set МANAGER = " +
                                "(select t.МANAGER from ext.SALESREPS t where s.EMPL_NUМ = t.EMPL_NUМ) ";
                        stat.executeUpdate(query);
                    }

                    // Подтверждение транзакции
                    conn.commit();
                }
                catch (SQLException e)
                {
                    conn.rollback();
                    for (Throwable t : e)
                        System.out.println(t.getMessage());
                }
                finally {
                    conn.setAutoCommit(true);
                }

                // вывести содержимое таблиц БД ed2
                try (Statement stat = conn.createStatement())
                {
                    // PRODUCTS
                    String query = "select * from public.PRODUCTS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // OFFICES
                    query = "select * from public.OFFICES";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // SALESREPS
                    query = "select * from public.SALESREPS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // CUSTOMERS
                    query = "select * from public.CUSTOMERS";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        DBUtils.printTable(rs);
                    }

                    // ORDERS
                    query = "select * from public.ORDERS";
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

}
