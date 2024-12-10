package precio.precios.rabbit;

import precio.precios.security.TokenService;
import precio.precios.utils.rabbit.FanoutConsumer;
import precio.precios.utils.rabbit.RabbitEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumeLogout {

    @Autowired
    TokenService tokenService;

    @Autowired
    FanoutConsumer fanoutConsumer;

    public void init() {
        fanoutConsumer.init("auth")
                .addProcessor("logout", this::processLogout)
                .start();
    }

    void processLogout(RabbitEvent event) {
        tokenService.invalidate(event.message.toString());
    }

}
