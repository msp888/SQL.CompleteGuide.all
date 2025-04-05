package task_13_02;

import task.utils.db.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Task_13_02 {


    public static void main(String[] args) throws IOException, SQLException {

        // используя скрипт "db_Educational.sql" для информации, создать в БД ed2
        //		такие же таблицы как и в БД educational (в том числе ключи и ограничения)
        try (Connection conn = DBUtils.getConnection("ed2"))
        {
            try (Statement stat = conn.createStatement())
            {
                String query =  "CREATE TABLE PRODUCTS " +
                                "( " +
                                    "MFR_ID CHAR(3) NOT NULL, " +
                                    "PRODUCT_ID CHAR(5) NOT NULL, " +
                                    "DESCRIPTION VARCHAR(20) NOT NULL, " +
                                    "PRICE DECIMAL(9,2) NOT NULL, " +
                                    "QTY_ON_HAND INTEGER NOT NULL, " +
                                    "PRIMARY KEY (MFR_ID, PRODUCT_ID) " +
                                ")";
                stat.executeUpdate(query);

                query =         "CREATE TABLE OFFICES " +
                                "( " +
                                    "OFFICE INTEGER NOT NULL, " +
                                    "CITY VARCHAR(15) NOT NULL, " +
                                    "REGION VARCHAR(10) NOT NULL, " +
                                    "MGR INTEGER, " +
                                    "TARGET DECIMAL(9,2), " +
                                    "SALES DECIMAL(9,2) NOT NULL, " +
                                    "PRIMARY KEY (OFFICE) " +
                                ")";
                stat.executeUpdate(query);

                query =         "CREATE TABLE SALESREPS " +
                                "( " +
                                    "EMPL_NUМ INTEGER NOT NULL, " +
                                    "NAME VARCHAR(15) NOT NULL, " +
                                    "AGE INTEGER, " +
                                    "REP_OFFICE INTEGER, " +
                                    "TITLE VARCHAR(10), " +
                                    "HIRE_DATE DATE NOT NULL, " +
                                    "МANAGER INTEGER, " +
                                    "QUOTA DECIMAL(9,2), " +
                                    "SALES DECIMAL(9,2) NOT NULL, " +
                                    "PRIMARY KEY (EMPL_NUМ) " +
                                ")";
                stat.executeUpdate(query);

                query =         "CREATE TABLE CUSTOMERS " +
                                "( " +
                                     "CUST_NUМ INTEGER NOT NULL, " +
                                     "COMPANY VARCHAR(20) NOT NULL, " +
                                     "CUST_REP INTEGER, " +
                                     "CREDIT_LIMIT DECIMAL(9,2), " +
                                     "PRIMARY KEY (CUST_NUМ), " +
                                     "FOREIGN KEY (CUST_REP) REFERENCES SALESREPS(EMPL_NUМ) ON DELETE SET NULL " +
                                ")";
                stat.executeUpdate(query);

                query =         "CREATE TABLE ORDERS " +
                                "( " +
                                    "ORDER_NUМ INTEGER NOT NULL, " +
                                    "ORDER_DATE DATE NOT NULL, " +
                                    "CUST INTEGER NOT NULL, " +
                                    "REP INTEGER, " +
                                    "MFR CHAR(3) NOT NULL, " +
                                    "PRODUCT CHAR (5) NOT NULL, " +
                                    "QTY INTEGER NOT NULL, " +
                                    "AМOUNT DECIMAL(9,2) NOT NULL, " +
                                    "PRIMARY KEY (ORDER_NUМ), " +
                                    "FOREIGN KEY (CUST)\tREFERENCES CUSTOMERS ON DELETE CASCADE, " +
                                    "FOREIGN KEY (REP)\tREFERENCES SALESREPS ON DELETE SET NULL, " +
                                    "FOREIGN KEY (MFR , PRODUCT ) REFERENCES PRODUCTS ON DELETE NO ACTION " +
                                ")";
                stat.executeUpdate(query);

                query =         "ALTER TABLE OFFICES " +
                                    "ADD CONSTRAINT НASMGR FOREIGN KEY (MGR) REFERENCES SALESREPS(EMPL_NUМ) ON DELETE SET NULL";
                stat.executeUpdate(query);

                query =         "ALTER TABLE SALESREPS " +
                                    "ADD CONSTRAINT SALESREPS_x1 FOREIGN KEY (МANAGER) REFERENCES SALESREPS(EMPL_NUМ) ON DELETE NO ACTION";
                stat.executeUpdate(query);

                query =         "ALTER TABLE SALESREPS " +
                                    "ADD CONSTRAINT SALESREPS_x2 FOREIGN KEY (REP_OFFICE) REFERENCES OFFICES(OFFICE) ON DELETE SET NULL";
                stat.executeUpdate(query);
            }
        }
        catch (SQLException e)
        {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

}
