package org.RabbitMQ;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author tiger.wang
 * @version 1.0
 * @date 2021/3/12 11:20
 */
public class ConnectUtil {

//    private static final String RABBIT_HOST = "172.16.200.68";
//
//    private static final String RABBIT_USERNAME = "worth1";
//
//    private static final String RABBIT_PASSWORD = "123";

    private static Connection connection = null;

    public static Connection getConnection(String RABBIT_HOST,String RABBIT_USERNAME,String RABBIT_PASSWORD) {
        if(connection == null) {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(RABBIT_HOST);
            connectionFactory.setUsername(RABBIT_USERNAME);
            connectionFactory.setPassword(RABBIT_PASSWORD);
            try {
                connection = connectionFactory.newConnection();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

}
