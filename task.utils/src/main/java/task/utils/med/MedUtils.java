package task.utils.med;

import task.utils.db.*;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MedUtils implements AutoCloseable {

    private final Connection conn; // Объект управления подключением к БД

    public MedUtils() throws SQLException, IOException {
        conn = DBUtils.getConnection();
    }

    public void close() throws Exception {
        conn.close();
    }

    /** Получить идентификатор должности по указанному имени должности */
    public int getIDPosition(String namePosition) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select ID_Position " +
                                         "from tbl_Positions " +
                                         "where name = '%s'", namePosition);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getInt("ID_Position");
                else
                    return 0;
            }
        }
    }

    /** Получить количество рабочих мест */
    public int getWorkplaces(int idPosition) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select Workplaces " +
                                         "from tbl_Positions " +
                                         "where ID_Position = %d", idPosition);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getInt("Workplaces");
                else
                    return 0;
            }
        }
    }

    /** Получить количество свободных рабочих мест */
    public int getFreePlaces(int idPosition) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select (p.Workplaces - COALESCE(e.used, 0)) as freePlaces " +
                                         "from tbl_Positions p left outer join " +
                                                "(select ID_Position, count(ID_Position) as used " +
                                                " from tbl_Employees " +
                                                " group by ID_Position " +
                                                ") e " +
                                            "on p.ID_Position = e.ID_Position " +
                                         "where p.ID_Position = %d", idPosition);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getInt("freePlaces");
                else
                    return 0;
            }
        }
    }

    /** Получить количество свободных рабочих мест */
    public int getFreePlaces(String namePosition) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select (p.Workplaces - COALESCE(e.used, 0)) as freePlaces " +
                                         "from tbl_Positions p left outer join " +
                                                "(select ID_Position, count(ID_Position) as used " +
                                                " from tbl_Employees " +
                                                " group by ID_Position " +
                                                ") e " +
                                            "on p.ID_Position = e.ID_Position " +
                                         "where p.Name = '%s'", namePosition);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getInt("freePlaces");
                else
                    return 0;
            }
        }
    }

    /** Добавить должность и количество рабочих мест для этой должности */
    public int addPosition(String namePosition, int workplaces) throws SQLException
    {
        int id = getIDPosition(namePosition);
        if (id != 0)
            return id;

        try (Statement stat = conn.createStatement())
        {
            String query = String.format("insert into tbl_Positions (Name, Workplaces) " +
                                         "values ('%s', %d)", namePosition, workplaces);
            if (stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                try (ResultSet rs = stat.getGeneratedKeys())
                {
                    if (rs.next())
                        return rs.getInt(1);
                    else
                        return 0;
                }
            }
            else
                return 0;
        }
    }

    /** Получить идентификатор сотрудника по указанным: фамилии, имени и отчеству */
    public int getIDEmployee(String surname, String name, String middleName) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select ID_Employee " +
                                         "from tbl_Employees " +
                                         "where Surname = '%s' and " +
                                              " Name = '%s' and " +
                                              " MiddleName = '%s'", surname, name, middleName);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getInt("ID_Employee");
                else
                    return 0;
            }
        }
    }

    /** Получить наименование должности сотрудника по идентификатору сотрудника */
    public String getEmployeePosition(int idEmployee) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("select p.Name " +
                                         "from tbl_Employees e inner join tbl_Positions p " +
                                          " on e.ID_Position = p.ID_Position " +
                                         "where e.ID_Employee = %d", idEmployee);
            try (ResultSet rs = stat.executeQuery(query))
            {
                if (rs.next())
                    return rs.getString("Name");
                else
                    return "";
            }
        }
    }

    /** Добавить сотрудника в БД */
    public int addEmployee(String surname,
                               String name,
                               String middleName,
                               Calendar dateOfBirth,
                               Calendar employmentDate,
                               int idPosition
                               ) throws SQLException
    {
        int id = getIDEmployee(surname, name, middleName);
        if (id != 0)
            return id;

        try (Statement stat = conn.createStatement())
        {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

            String query = String.format("insert into tbl_Employees " +
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
                    if (rs.next())
                        return rs.getInt(1);
                    else
                        return 0;
                }
            }
            else
                return 0;
        }
    }

    /** Удалить должность */
    public int deletePosition(int idPosition) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("delete from tbl_Positions " +
                                         "where ID_Position = %d", idPosition);
            return stat.executeUpdate(query);
        }
    }

    /** Удалить сотрудника */
    public int deleteEmployee(int idEmployee) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("delete from tbl_Employees " +
                    "where ID_Employee = %d", idEmployee);
            return stat.executeUpdate(query);
        }
    }

    /** Копировать накладную */
    public int copyOrder(int idOrder, Calendar date, double m) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            int res = 0;
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

            String query = String.format("insert into tbl_Orders (ID_StorageArea, ID_OrderState, DateOfOperation, ID_Source) " +
                    "select ID_StorageArea, ID_OrderState, '%s', ID_Source " +
                    "from tbl_Orders " +
                    "where ID_Order = %d", formater.format(date.getTime()), idOrder);

            if (stat.executeUpdate(query, Statement.RETURN_GENERATED_KEYS) > 0) {
                try (ResultSet rs = stat.getGeneratedKeys())
                {
                    if (rs.next())
                        res = rs.getInt(1);
                    else
                        return 0;
                }
            }
            else
                return 0;

            query = String.format("insert into tbl_OrderItems " +
                    "(ID_Order, ID_Product, ID_TransportUnit, Quantity, Price, DateOfManufacture, SellBy, StorageTemperature) " +
                    "select %d, ID_Product, ID_TransportUnit, Quantity, Price * %s, DateOfManufacture, SellBy, StorageTemperature " +
                    "from tbl_OrderItems " +
                    "where ID_Order = %d", res, String.valueOf(m), idOrder);
            stat.executeUpdate(query);

            return res;
        }
    }

    /** Удалить накладную */
    public void deleteOrder(int idOrder) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("delete from tbl_StorageAreaItems " +
                                         "where ID_OrderItem in " +
                                                             "(select ID_OrderItem " +
                                                             " from tbl_OrderItems " +
                                                             " where ID_Order = %d)", idOrder);
            stat.executeUpdate(query);

            query = String.format("delete from tbl_OrderItems " +
                                  "where ID_Order = %d", idOrder);
            stat.executeUpdate(query);

            query = String.format("delete from tbl_Orders " +
                                  "where ID_Order = %d", idOrder);
            stat.executeUpdate(query);
        }
    }

    /** Изменить количество рабочих мест для должности */
    public int updateWorkplaces(int idPosition, int workplaces) throws SQLException
    {
        /* если новое количество рабочих мест для должности меньше чем уже занято
	       рабочих мест, то функция возвращает текущее количество рабочих мест для должности */
        int all  = getWorkplaces(idPosition);
        int curr = getFreePlaces(idPosition);
        if (workplaces < (all - curr)) {
            return all;
        }

        try (Statement stat = conn.createStatement())
        {
            String query = String.format("update tbl_Positions set Workplaces = %d " +
                                         "where ID_Position = %d", workplaces, idPosition);
            if (stat.executeUpdate(query) > 0) {
                return workplaces;
            }
            else
                return all;
        }
    }

    /** Цену каждой позиции в указанной накладной умножаться на заданный коэффициент */
    public int multPriceInOrder(int idOrder, double m) throws SQLException
    {
        try (Statement stat = conn.createStatement())
        {
            String query = String.format("update tbl_OrderItems set Price = Price * %s " +
                                         "where ID_Order = %d", String.valueOf(m), idOrder);
            return stat.executeUpdate(query);
        }
    }


}
