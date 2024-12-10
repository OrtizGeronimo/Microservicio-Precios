package precio.precios.rabbit;


import precio.precios.server.PaymentMethodLogger;
import precio.precios.server.ValidatorService;
import precio.precios.utils.rabbit.DirectConsumer;
import precio.precios.utils.rabbit.DirectPublisher;
import precio.precios.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitService {

    /*Aquí se encuentran todos los eventos que envía y recibe el microservicio*/

    @Autowired
    DirectPublisher directPublisher;
    @Autowired
    ValidatorService validatorService;
    @Autowired
    PaymentMethodLogger paymentMethodLogger;
//    @Autowired
//    PaymentMethodRepo paymentMethodRepo;
    @Autowired
    DirectConsumer directConsumer;
//    @Autowired
//    PaymentMethodService paymentMethodService;

    public void init() {
        directConsumer.init("payment_method", "payment_method")
                .addProcessor("payment_undefined", this::proccessPaymentUndefined)
                .start();
    }

    void proccessPaymentUndefined(RabbitEvent rabbitEvent){
        /*PaymentUndefinedEvent paymentUndefinedEvent = PaymentUndefinedEvent.fromJson(rabbitEvent.message.toString());
        try{
            paymentMethodService.findAllByUser()
        } catch (Exception e){

        }*/
    }

    /*public void publishPaymentMethod(String exchange, String queue, PublishPaymentMethodDataEvent send){
        RabbitEvent eventToSend = new RabbitEvent();
        eventToSend.type = "payment-method-defined";
        eventToSend.message = send;

        directPublisher.publish(exchange, queue, eventToSend);
    }*/
}
