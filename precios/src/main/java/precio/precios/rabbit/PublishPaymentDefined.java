package precio.precios.rabbit;

import precio.precios.rabbit.dto.PublishPaymentDefinedData;
import precio.precios.utils.rabbit.DirectPublisher;
import precio.precios.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublishPaymentDefined {

    @Autowired
    DirectPublisher directPublisher;

    public void publish(String exchange, String queue, PublishPaymentDefinedData send) {
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "payment-defined";
        eventToSend.message = send;

        directPublisher.publish(exchange, queue, eventToSend);
    }
}
