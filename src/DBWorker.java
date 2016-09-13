import java.sql.*;


/**
 * Created by sanak on 18.03.2016.
 */
public class DBWorker {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection dbConnection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users?autoReconnect=true&useSSL=true","root", "root");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
