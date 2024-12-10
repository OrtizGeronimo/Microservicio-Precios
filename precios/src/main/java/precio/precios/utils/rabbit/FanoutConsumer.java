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

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FanoutConsumer {

    @Autowired
    EnvironmentVars environmentVars;

    @Autowired
    ValidatorService validatorService;

    @Autowired
    PaymentMethodLogger paymentMethodLogger;

    private String exchange;
    private final Map<String, EventProcessor> listeners = new HashMap<>();

    public FanoutConsumer init(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public FanoutConsumer addProcessor(String event, EventProcessor listener) {
        listeners.put(event, listener);
        return this;
    }

    public void startDelayed() {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                start();
            }
        }, 10 * 1000); // En 10 segundos reintenta.
    }

    public void start() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(environmentVars.envData.rabbitServerUrl);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.exchangeDeclare(exchange, "fanout");
            String queueName = channel.queueDeclare("", false, false, false, null).getQueue();

            channel.queueBind(queueName, exchange, "");

            new Thread(() -> {
                try {
                    paymentMethodLogger.info("RabbitMQ Fanout Conectado");

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, //
                                                   Envelope envelope, //
                                                   AMQP.BasicProperties properties, //
                                                   byte[] body) {
                            try {
                                RabbitEvent event = RabbitEvent.fromJson(new String(body));

                                EventProcessor eventConsumer = listeners.get(event.type);
                                if (eventConsumer != null) {
                                    paymentMethodLogger.info("RabbitMQ Consume " + event.type);

                                    eventConsumer.process(event);
                                }
                            } catch (Exception e) {
                                paymentMethodLogger.error("RabbitMQ Logout", e);
                            }
                        }
                    };
                    channel.basicConsume(queueName, true, consumer);
                } catch (Exception e) {
                    paymentMethodLogger.info("RabbitMQ ArticleValidation desconectado");
                    startDelayed();
                }
            }).start();
        } catch (Exception e) {
            paymentMethodLogger.error("RabbitMQ ArticleValidation desconectado", e);
            startDelayed();
        }
    }
}
