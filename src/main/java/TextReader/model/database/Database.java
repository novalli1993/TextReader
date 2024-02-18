package TextReader.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    String URL = "jdbc:mysql://localhost:3306/";
    String USER = "kephan";
    String PASSWORD = "c19931215c";
    String ROOT_USER = "root";
    String ROOT_PASSWORD = "c19931215c";

    public void InitDatabase(String database) {
        try (Connection conn = DriverManager.getConnection(URL, ROOT_USER, ROOT_PASSWORD)) {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + database + ";");
            stmt.executeUpdate("GRANT ALL ON reader.* TO " + USER + ";");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
