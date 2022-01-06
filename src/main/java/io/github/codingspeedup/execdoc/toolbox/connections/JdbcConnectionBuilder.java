package io.github.codingspeedup.execdoc.toolbox.connections;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;
import java.util.Properties;

public class JdbcConnectionBuilder {

    private final String engine;
    private String url;
    private String user;
    private String password;
    private Properties properties;

    public JdbcConnectionBuilder(String engine) {
        this.engine = engine;
    }

    public static JdbcConnectionBuilder toMySql(String url) {
        return to("jdbc:mysql:" + url);
    }

    public static JdbcConnectionBuilder to(String url) {
        String[] chunks = url.split(":", 3);
        JdbcConnectionBuilder builder = new JdbcConnectionBuilder(chunks[1].toLowerCase(Locale.ROOT));
        builder.url = url;
        return builder;
    }

    public JdbcConnectionBuilder user(String user) {
        this.user = user;
        return this;
    }

    public JdbcConnectionBuilder password(String password) {
        this.password = password;
        return this;
    }

    public JdbcConnectionBuilder setProperty(String key, String value) {
        if (properties == null) {
            properties = new Properties();
        }
        properties.setProperty(key, value);
        return this;
    }

    @SneakyThrows
    public Connection build() {
        Connection connection;
        if (properties != null) {
            connection = DriverManager.getConnection(url, properties);
        } else if (StringUtils.isNoneBlank(user, password)) {
            connection = DriverManager.getConnection(url, user, password);
        } else {
            connection = DriverManager.getConnection(url);
        }
        return (Connection) Proxy.newProxyInstance(
                JdbcConnectionBuilder.class.getClassLoader(),
                new Class[]{Connection.class},
                new JdbcConnectionProxy(connection));
    }

}
