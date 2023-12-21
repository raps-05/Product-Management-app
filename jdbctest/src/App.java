// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.SQLException;
// import java.sql.Statement;

// public class App {
//     public static void main(String[] args) {
//         // Database connection properties
//         String url = "jdbc:mysql://localhost/";
//         String username = "FirstMySQLconnection";
//         String password = "";

//         try (Connection connection = DriverManager.getConnection(url, username, password)) {
//             System.out.println("Connected to MySQL server.");

//             // Create a new database
//             String databaseName = "mydb2";
//             String createDatabaseQuery = "CREATE DATABASE " + databaseName;
//             try (Statement statement = connection.createStatement()) {
//                 statement.executeUpdate(createDatabaseQuery);
//                 System.out.println("Database '" + databaseName + "' created successfully.");

//                 // Use the newly created database
//                 statement.execute("USE " + databaseName);

//                 // Create tables
//                 String createTableQuery = "CREATE TABLE users (" +
//                         "id INT PRIMARY KEY AUTO_INCREMENT," +
//                         "name VARCHAR(50)," +
//                         "age INT" +
                        
//                         ")";
//                 statement.executeUpdate(createTableQuery);
//                 System.out.println("Table 'users' created successfully.");
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }


import java.sql.*;

public class App {
    static final String QUERY = "SELECT id, first, last, age FROM REG_EMP";

    public static void main(String[] args) {
        Connection con;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb1", "FirstMySQLconnection", "");
            Statement stmt = con.createStatement();

            // Create table
            String sql = "CREATE TABLE EMP_REG " +
                         "(id INTEGER not NULL, " +
                         "first VARCHAR(225), " +
                         "last VARCHAR(225), " +
                         "PRIMARY KEY(ID))";
            stmt.executeUpdate(sql);
            System.out.println("Created table in the given database....");

            stmt.close();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}