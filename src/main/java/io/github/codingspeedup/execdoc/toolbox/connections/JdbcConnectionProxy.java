package io.github.codingspeedup.execdoc.toolbox.connections;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

public class JdbcConnectionProxy implements InvocationHandler {

    private final Connection connection;

    public JdbcConnectionProxy(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(connection, args);
    }

}
