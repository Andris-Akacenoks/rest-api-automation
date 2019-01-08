package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {
    private static final String BASE_URL = "api.baseUrl";

    private static Config INSTANCE;
    private final Properties appProps;

    private Config() throws IOException {
        final String fileName = System.getProperty("env") + ".properties";
        final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
        appProps = new Properties();
        if (inputStream != null) {
            appProps.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
        }
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            synchronized (Config.class) {
                if (INSTANCE == null) {
                    try {
                        INSTANCE = new Config();
                    } catch (final IOException e) {
                        throw new RuntimeException("Failed to load configuration : " + e.getMessage());
                    }
                }
            }
        }
        return INSTANCE;
    }

    public String getApiBaseUrl() {
        return appProps.getProperty(BASE_URL);
    }
}