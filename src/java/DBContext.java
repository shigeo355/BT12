import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    public static void main(String[] args) {
        testDirectConnection();
        //testConnectionPool();
    }
    
    public static void testDirectConnection() {
        try {
            String dbURL = "jdbc:sqlserver://localhost:1433;databaseName=murach;encrypt=false";
            String username = "test";
            String password = "12345";

            Connection connection = DriverManager.getConnection(dbURL, username, password);
            System.out.println("Direct connection: SUCCESS");
            connection.close();
        } catch (SQLException e) {
            System.err.println("Direct connection: FAILED - " + e.getMessage());
        }
    }
    
}