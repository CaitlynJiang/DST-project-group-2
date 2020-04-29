package DST2.Group2.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(DST2.Group2.servlet.AppConfig.class);
    private static final DST2.Group2.servlet.AppConfig instance = new DST2.Group2.servlet.AppConfig();

    public static DST2.Group2.servlet.AppConfig getInstance() {
        return instance;
    }

    public AppConfig() {
        InputStream resourceAsStream = null;
        try {
            resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties");
            Properties properties = new Properties();
            try {
                properties.load(resourceAsStream);
                this.jdbcUrl = properties.getProperty("jdbc.url");
                this.jdbcUsername = properties.getProperty("jdbc.username");
                this.jdbcPassword = properties.getProperty("jdbc.password");
            } catch (IOException e) {
                log.info("", e);
            }
        } finally {
            if (resourceAsStream != null) {
                try {
                    resourceAsStream.close();
                } catch (IOException e) {
                    log.info("", e);
                }
            }
        }
    }

    private String jdbcUrl;
    private String jdbcUsername;
    private String jdbcPassword;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUsername() {
        return jdbcUsername;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public String getJdbcPassword() {
        return jdbcPassword;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

}
