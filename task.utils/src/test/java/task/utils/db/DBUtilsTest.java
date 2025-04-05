package task.utils.db;

import order.test.classes.TestClassesOrder;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@DisplayName("Проверка класса DBUtils")
@TestClassesOrder(1)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DBUtilsTest {

    private int getNumberOfRecords(ResultSet rs) throws SQLException {
        int numberOfRecords = 0;

        while (rs.next()) {
            numberOfRecords++;
        }

        return numberOfRecords;
    }

    @Test
    @Order(1)
    @DisplayName("Проверка получения типа СУБД")
    public void checkGetTypeDBMS() {
        Assertions.assertNotEquals(TypeDBMS.UNKNOWN, DBUtils.getTypeDBMS(), "Тип СУБД должен быть определен");
    }

    @Test
    @Order(2)
    @DisplayName("Подключение к БД по умолчанию, т.е. к wms_medicines")
    public void defaultConnection() {
        // Assertions.fail("Тест не реализован...");
        try (Connection conn = DBUtils.getConnection())
        {
            try (Statement stat = conn.createStatement())
            {
                String query = "select * from tbl_Positions";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    Assertions.assertTrue(getNumberOfRecords(rs) > 0);
                }
            }
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Явное подключение к БД wms_medicines")
    public void connectTo_wms_medicines() {
        try (Connection conn = DBUtils.getConnection("wms_medicines"))
        {
            try (Statement stat = conn.createStatement())
            {
                String query = "select * from tbl_Positions";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    Assertions.assertTrue(getNumberOfRecords(rs) > 0);
                }
            }
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Подключение к БД educational")
    public void connectTo_educational() {
        try (Connection conn = DBUtils.getConnection("educational"))
        {
            try (Statement stat = conn.createStatement())
            {
                String query = "select * from OFFICES";
                try (ResultSet rs = stat.executeQuery(query))
                {
                    Assertions.assertTrue(getNumberOfRecords(rs) > 0);
                }
            }
        }
        catch (Exception e)
        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Получить подключение к СУБД (без указания БД)")
    public void connectTo_DBMS() {

        if ( DBUtils.getTypeDBMS() == TypeDBMS.MSSQL ) {
            try (Connection conn = DBUtils.getConnectionDBMS())
            {
                try (Statement stat = conn.createStatement())
                {
                    // educational: Если схема известна, например, это dbo
                    String query = "select * from educational.dbo.OFFICES";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        Assertions.assertTrue(getNumberOfRecords(rs) > 0, "Если схема известна, например, это dbo");
                    }

                    // educational: Если схема неизвестна
                    query = "select * from educational..OFFICES";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        Assertions.assertTrue(getNumberOfRecords(rs) > 0, "Если схема неизвестна");
                    }

                    // wms_medicines
                    query = "select * from wms_medicines..tbl_Positions";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        Assertions.assertTrue(getNumberOfRecords(rs) > 0, "wms_medicines");
                    }

                    // Можно сразу из нескольких БД
                    query = "select * from wms_medicines..tbl_Positions, educational..OFFICES";
                    try (ResultSet rs = stat.executeQuery(query))
                    {
                        Assertions.assertTrue(getNumberOfRecords(rs) > 0, "Можно сразу из нескольких БД");
                    }
                }
            }
            catch (Exception e)
            {
                Assertions.fail(e.getMessage(), e);
            }
        }
        else {
            try (Connection conn = DBUtils.getConnectionDBMS())
            {
                try (Statement stat = conn.createStatement())
                {
                    // создать базу данных
                    String query =  "create database ed99 ";
                    stat.executeUpdate(query);

                    // удалить базу данных
                    query =  "drop database ed99 ";
                    stat.executeUpdate(query);

                    Assertions.assertTrue(true);
                }
            }
            catch (Exception e)
            {
                Assertions.fail(e.getMessage(), e);
            }
        }
    }
}
