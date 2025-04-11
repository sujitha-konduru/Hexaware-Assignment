package utility;

import java.io.FileInputStream;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String fileName) throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        props.load(fis);
        return props.getProperty("db.url") + "?user=" + props.getProperty("db.user") + "&password=" + props.getProperty("db.password");
    }
}
