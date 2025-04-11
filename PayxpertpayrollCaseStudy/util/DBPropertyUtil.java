package util;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            properties.load(fis);
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            return url + "?user=" + user + "&password=" + password;
        } catch (IOException e) {
            throw new RuntimeException("Error reading database properties file", e);
        }
    }
}