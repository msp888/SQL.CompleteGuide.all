package task.utils.med;

import order.test.classes.TestClassesOrder;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@DisplayName("Проверка класса MedUtils")
@TestClassesOrder(2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MedUtilsTest {

    private int idCopyOrder; // Идентификатор копии накладной
    private static int workplacesRestory;  // Количество рабочих мест для восстановления

    @Test
    @Order(1)
    @DisplayName("Проверка подключение к БД")
    public void checkConnection() {
        try (MedUtils mu = new MedUtils()) {
            // Assertions.fail("Тест не реализован...");
            Assertions.assertTrue(true);
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(2)
    @DisplayName("Получить идентификатор должности по указанному имени должности")
    public void checkGetIDPosition() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего сотрудника
            String posName = "Кладовщик";
            Assertions.assertEquals(2,
                    mu.getIDPosition(posName),
                    String.format("проверка идентификатора для должности: %s", posName)
            );

            // Для несуществующего сотрудника
            posName = "###-тестовое-значение-###";
            Assertions.assertEquals(0,
                    mu.getIDPosition(posName),
                    String.format("проверка идентификатора для должности: %s", posName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Получить общее количество рабочих мест")
    public void checkGetWorkplaces() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего сотрудника
            String posName = "Бригадир грузчиков";
            Assertions.assertEquals(2,
                    mu.getWorkplaces(mu.getIDPosition(posName)),
                    String.format("проверка общего количества рабочих мест для должности: %s", posName)
            );

            // Для несуществующего сотрудника
            posName = "###-тестовое-значение-###";
            Assertions.assertEquals(0,
                    mu.getWorkplaces(mu.getIDPosition(posName)),
                    String.format("проверка общего количества рабочих мест для должности: %s", posName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Получить количество свободных рабочих мест по идентификатору")
    public void checkGetFreePlacesFromId() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего идентификатора
            int id = 1;
            Assertions.assertEquals(22,
                    mu.getFreePlaces(id),
                    String.format("проверка количества свободных рабочих мест по идентификатору: %d", id)
            );

            // Для несуществующего идентификатора
            id = 0;
            Assertions.assertEquals(0,
                    mu.getFreePlaces(id),
                    String.format("проверка количества свободных рабочих мест по идентификатору: %d", id)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Получить количество свободных рабочих мест по наименованию должности")
    public void checkGetFreePlacesFromNamePosition() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего сотрудника
            String posName = "Сборщик";
            Assertions.assertEquals(22,
                    mu.getFreePlaces(posName),
                    String.format("проверка количества свободных рабочих мест по наименованию должности: %s", posName)
            );

            // Для несуществующего сотрудника
            posName = "###-тестовое-значение-###";
            Assertions.assertEquals(0,
                    mu.getFreePlaces(posName),
                    String.format("проверка количества свободных рабочих мест по наименованию должности: %s", posName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Добавить должность и количество рабочих мест для этой должности")
    public void checkAddPosition() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующей должности
            String posName = "Грузчик";
            // получаем параметры существующей должности
            int id = mu.getIDPosition(posName);
            int wp = mu.getWorkplaces(id);
            Assertions.assertEquals(id,
                    mu.addPosition(posName, wp + 20),
                    String.format("идентификатор должности: %s, не должен измениться", posName)
            );
            Assertions.assertEquals(wp,
                    mu.getWorkplaces(id),
                    String.format("количество рабочих мест должности: %s, не должно измениться", posName)
            );

            // Для несуществующей должности
            posName = "Водитель";
            wp = 8; // количество рабочих мест для создаваемой должности
            id = mu.addPosition(posName, wp);
            Assertions.assertTrue(id > 0,
                    String.format("идентификатор должности: %s, должен быть больше 0", posName)
            );
            Assertions.assertEquals(wp,
                    mu.getWorkplaces(id),
                    String.format("проверка заданного количество рабочих мест должности: %s", posName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Получить идентификатор сотрудника по указанным: фамилии, имени и отчеству")
    public void checkGetIDEmployee() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего сотрудника
            String surname = "Сидоров";
            String name = "Петр";
            String middleName = "Степанович";
            Assertions.assertEquals(2,
                    mu.getIDEmployee(surname, name, middleName),
                    String.format("проверка получения идентификатора сотрудника по ФИО: %s %s %s",
                            surname, name, middleName)
            );

            // Для несуществующего сотрудника
            surname = "###-тестовое-значение-###";
            name = "###-тестовое-значение-###";
            middleName = "###-тестовое-значение-###";
            Assertions.assertEquals(0,
                    mu.getIDEmployee(surname, name, middleName),
                    String.format("проверка получения идентификатора сотрудника по ФИО: %s %s %s",
                            surname, name, middleName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(8)
    @DisplayName("Получить наименование должности сотрудника по идентификатору сотрудника")
    public void checkGetEmployeePosition() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего идентификатора
            int id = 2;
            Assertions.assertEquals("Сборщик",
                    mu.getEmployeePosition(id),
                    String.format("проверка наименования должности сотрудника по идентификатору сотрудника: %d", id)
            );

            // Для несуществующего идентификатора
            id = 0;
            Assertions.assertEquals("",
                    mu.getEmployeePosition(id),
                    String.format("проверка наименования должности сотрудника по идентификатору сотрудника: %d", id)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(9)
    @DisplayName("Добавить сотрудника в БД")
    public void checkAddEmployee() {
        try (MedUtils mu = new MedUtils()) {
            // Для существующего сотрудника
            String surname = "Сидоров";
            String name = "Петр";
            String middleName = "Степанович";
            int id = mu.getIDEmployee(surname, name, middleName);
            Assertions.assertEquals(id,
                    mu.addEmployee(surname, name, middleName,
                            new GregorianCalendar(1980, Calendar.JANUARY, 1),
                            new GregorianCalendar(2024, Calendar.OCTOBER, 2),
                            mu.getIDPosition("Водитель")),
                    String.format("проверка создания существующего сотрудника: %s %s %s", surname, name, middleName)
            );

            // Для несуществующего сотрудника
            surname = "Андреев";
            name = "Андрей";
            middleName = "Андреевич";
            Assertions.assertTrue(mu.addEmployee(surname, name, middleName,
                    new GregorianCalendar(1980, Calendar.JANUARY, 1),
                    new GregorianCalendar(2024, Calendar.OCTOBER, 2),
                    mu.getIDPosition("Водитель"))  > 0,
                    String.format("проверка создания нового сотрудника: %s %s %s", surname, name, middleName)
            );

            // ... конечно же мы должны проверить все параметры добавленного сотрудника
            // а также ошибки, которые должны формироваться при неправильных входных параметрах...
            // ...но время идет...
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(10)
    @DisplayName("Удалить должность")
    public void checkDeletePosition() {
        try (MedUtils mu = new MedUtils()) {
            // Должность, на которую не назначены сотрудники
            String posName = "Космонавт";
            int id = mu.addPosition(posName, 10);
            Assertions.assertEquals(1,
                    mu.deletePosition(id),
                    String.format("проверка удаления должности: %s, на которую не назначены сотрудники", posName)
            );

            // Должность, на которую назначены сотрудники
            posName = "Водитель";
            int id2 = mu.getIDPosition(posName);
            // Assertions.assertThrowsExactly !!! - нельзя, потому что возвращается унаследованное от SQLException исключение
            Assertions.assertThrows(SQLException.class,
                    () -> {
                        mu.deletePosition(id2);
                    },
                    String.format("проверка удаления должности: %s, на которую назначены сотрудники", posName));
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(11)
    @DisplayName("Удалить сотрудника")
    public void checkDeleteEmployee() {
        try (MedUtils mu = new MedUtils()) {
            String surname = "Андреев";
            String name = "Андрей";
            String middleName = "Андреевич";
            int id = mu.getIDEmployee(surname, name, middleName);
            Assertions.assertEquals(1,
                    mu.deleteEmployee(id),
                    String.format("проверка удаления сотрудника: %s %s %s", surname, name, middleName)
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(12)
    @DisplayName("Копировать накладную")
    public void checkCopyOrder() {
        try (MedUtils mu = new MedUtils()) {
            idCopyOrder = mu.copyOrder(147,
                    new GregorianCalendar(2024, Calendar.OCTOBER, 19),
                    1.1
            );
            Assertions.assertTrue( idCopyOrder > 0, "проверка копирования накладной");
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(13)
    @DisplayName("Удалить накладную")
    public void checkDeleteOrder() {
        try (MedUtils mu = new MedUtils()) {
            // ...проверяем, что просто нет исключения, но если делать правильно, нужно проверить, что накладной нет в БД...
            mu.deleteOrder(idCopyOrder);
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(14)
    @DisplayName("Изменить количество рабочих мест для должности")
    public void checkUpdateWorkplaces() {
        try (MedUtils mu = new MedUtils()) {

            String posName = "Сборщик";
            int id = mu.getIDPosition(posName);

            // Количество рабочих мест для восстановления
            workplacesRestory = mu.getWorkplaces(id);

            // Пытаемся установить новое количество рабочих мест меньше, чем занято рабочих мест
            int workplaces = 1;
            Assertions.assertEquals(workplacesRestory,
                    mu.updateWorkplaces(id, workplaces),
                    "установить новое количество рабочих мест меньше, чем занято рабочих мест"
            );

            // Пытаемся установить новое количество рабочих мест меньше исходного, но больше, чем занято рабочих мест
            workplaces = 12;
            Assertions.assertEquals(workplaces,
                    mu.updateWorkplaces(id, workplaces),
                    "установить новое количество рабочих мест меньше исходного, но больше, чем занято рабочих мест"
            );

        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Order(15)
    @DisplayName("Умножение цен в накладной на заданный коэффициент")
    public void checkMultPriceInOrder() {
        try (MedUtils mu = new MedUtils()) {
            Assertions.assertTrue( mu.multPriceInOrder(146, 1.05) > 0,
                    "проверка умножения цен в накладной на заданный коэффициент"
            );
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @AfterAll
    public static void afterTesting() {
        try (MedUtils mu = new MedUtils()) {
            // Удалить должность "Водитель", чтобы почистить мусор...
            mu.deletePosition(mu.getIDPosition("Водитель"));

            // Восстановить исходное количество рабочих мест
            mu.updateWorkplaces(mu.getIDPosition("Сборщик"), workplacesRestory);
        }
        catch (Exception e)        {
            Assertions.fail(e.getMessage(), e);
        }
    }
}
