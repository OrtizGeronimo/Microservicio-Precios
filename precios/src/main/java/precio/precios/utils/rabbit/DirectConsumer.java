package precio.precios.utils.rabbit;


import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import precio.precios.server.EnvironmentVars;
import precio.precios.server.PaymentMethodLogger;
import precio.precios.server.ValidatorService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DirectConsumer {

    @Autowired
    EnvironmentVars environmentVars;

    @Autowired
    PaymentMethodLogger paymentMethodLogger;

    @Autowired
    ValidatorService validatorService;

    private String exchange;
    private String queue;
    private final Map<String, EventProcessor> listeners = new HashMap<>();

    public DirectConsumer init(String exchange, String queue) {
        this.exchange = exchange;
        this.queue = queue;
        return this;
    }

    public DirectConsumer addProcessor(String event, EventProcessor listener) {
        listeners.put(event, listener);
        return this;
    }
    public void startDelayed() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                start();
            }
        }, 10 * 1000);
    }

    public void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(environmentVars.envData.rabbitServerUrl);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "direct");
            channel.queueDeclare(queue, false, false, false, null);

            channel.queueBind(queue, exchange, queue);

            new Thread(() -> {
                try {
                    paymentMethodLogger.info("RabbitMQ Escuchando " + queue);

                    channel.basicConsume(queue, true, new EventConsumer(channel));
                } catch (Exception e) {
                    paymentMethodLogger.error("RabbitMQ ", e);
                    startDelayed();
                }
            }).start();
        } catch (Exception e) {
            paymentMethodLogger.error("RabbitMQ ", e);
            startDelayed();
        }
    }

    class EventConsumer extends DefaultConsumer {
        EventConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, //
                                   Envelope envelope, //
                                   AMQP.BasicProperties properties, //
                                   byte[] body) {
            try {
                RabbitEvent event = RabbitEvent.fromJson(new String(body));

                EventProcessor l = listeners.get(event.type);
                if (l != null) {
                    paymentMethodLogger.info("RabbitMQ Consume article-data : " + event.type);

                    l.process(event);
                }
            } catch (Exception e) {
                paymentMethodLogger.error("RabbitMQ ", e);
            }
        }
    }
}
