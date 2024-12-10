package precio.precios.utils.rabbit;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import precio.precios.server.EnvironmentVars;
import precio.precios.server.PaymentMethodLogger;
import precio.precios.utils.gson.GsonTools;

@Service
public class DirectPublisher {

    @Autowired
    PaymentMethodLogger paymentMethodLogger;

    @Autowired
    EnvironmentVars environmentVars;

    public void publish(String exchange, String queue, RabbitEvent message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(environmentVars.envData.rabbitServerUrl);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "direct");
            channel.queueDeclare(queue, false, false, false, null);

            channel.queueBind(queue, exchange, queue);

            channel.basicPublish(exchange, queue, null, GsonTools.toJson(message).getBytes());

            paymentMethodLogger.info("RabbitMQ Emit " + message.type);
        } catch (Exception e) {
            paymentMethodLogger.error("RabbitMQ no se pudo encolar " + message.type, e);
        }
    }
}
