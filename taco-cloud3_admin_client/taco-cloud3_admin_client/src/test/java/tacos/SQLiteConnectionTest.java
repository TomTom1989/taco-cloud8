package tacos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnectionTest {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // db parameters - use an in-memory database for testing
            String url = "jdbc:sqlite:mydatabase.db"; // Replace with your database path
            // create a connection to the database
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            // You can perform further operations here to test the database connectivity
            // ...

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}