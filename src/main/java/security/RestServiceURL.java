package security;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RestServiceURL {

    private static RestServiceURL instance = null;
    private static String baseUrl = null;

    public static RestServiceURL getInstance() {
        if (instance == null) {
            instance = new RestServiceURL();
            baseUrl = computeBaseUrl ();
        }
        return instance;
    }

    private static String computeBaseUrl () {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream input = classloader.getResourceAsStream("chess/configurations/config.properties");
        Properties properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty("restAddress");
    }

    public String getBaseUrl () {
        return baseUrl;
    }
}
