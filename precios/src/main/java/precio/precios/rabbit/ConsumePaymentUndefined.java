package precio.precios.rabbit;

import precio.precios.server.PaymentMethodLogger;
import precio.precios.server.ValidatorService;
import precio.precios.utils.rabbit.DirectConsumer;
import precio.precios.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumePaymentUndefined {

    @Autowired
    ValidatorService validatorService;

    @Autowired
    PaymentMethodLogger paymentMethodLogger;

    @Autowired
    DirectConsumer directConsumer;

//    @Autowired
//    OrderPaymentMethodRepo orderPaymentMethodRepo;

    public void init() {
        directConsumer.init("payment_method", "payment_method")
                .addProcessor("payment_undefined", this::proccessPaymentUndefined)
                .start();
    }

    void proccessPaymentUndefined(RabbitEvent rabbitEvent){
//        ConsumePaymentUndefinedData paymentUndefinedEvent = ConsumePaymentUndefinedData.fromJson(rabbitEvent.message.toString());
//        try{
//            OrderPaymentMethod nuevo = new OrderPaymentMethod();
//            nuevo.setOrderId(paymentUndefinedEvent.getOrderId());
//            orderPaymentMethodRepo.save(nuevo);
//        } catch (Exception e){
//            paymentMethodLogger.error(e.toString(), e);
//        }
    }
}