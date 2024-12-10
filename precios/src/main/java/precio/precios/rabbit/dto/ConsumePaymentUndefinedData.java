package precio.precios.rabbit.dto;

import precio.precios.utils.gson.GsonTools;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public final class ConsumePaymentUndefinedData {

    @SerializedName("orderId")
    private final String orderId;
    
    @SerializedName("token")
    private final String token;

    public ConsumePaymentUndefinedData(String orderId, String token){
        this.orderId = orderId;
        this.token = token;
    }

    public static ConsumePaymentUndefinedData fromJson(String json){
        return GsonTools.gson().fromJson(json, ConsumePaymentUndefinedData.class);
    }
}
