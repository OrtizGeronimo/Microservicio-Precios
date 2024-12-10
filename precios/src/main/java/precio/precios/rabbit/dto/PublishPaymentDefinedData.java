package precio.precios.rabbit.dto;

import precio.precios.rabbit.PublishPaymentDefined;
import com.google.gson.annotations.SerializedName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PublishPaymentDefinedData {

    @NotEmpty
    @SerializedName("orderId")
    private final String orderId;

    @NotEmpty
    @SerializedName("paymentMethodId")
    private final String paymentMethodId;

    public PublishPaymentDefinedData(String orderId, String paymentMethodId){
        this.orderId = orderId;
        this.paymentMethodId = paymentMethodId;
    }

    public void publishIn(PublishPaymentDefined publishPaymentDefined, String exchange, String queue){
        publishPaymentDefined.publish(exchange, queue, this);
    }
}
