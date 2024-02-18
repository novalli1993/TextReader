import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

public class TestSQL {
    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/TextReader/HikariConfig.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("jdbcUrl"));
        config.setUsername(props.getProperty("username"));
        config.setPassword(props.getProperty("password"));
        config.setConnectionTimeout(Long.parseLong(props.getProperty("connectionTimeout")));
        config.setIdleTimeout(Long.parseLong(props.getProperty("idleTimeout")));
        config.setMaximumPoolSize(Integer.parseInt((props.getProperty("maximumPoolSize"))));
        HikariDataSource ds = new HikariDataSource(config);
        try(Connection conn = ds.getConnection()){
            Statement stmt = conn.createStatement();
            try(ResultSet rs = stmt.executeQuery("show grants")) {
                if(rs.next()){
                    System.out.println(rs.getObject(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
