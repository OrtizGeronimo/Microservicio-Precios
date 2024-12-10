package precio.precios.utils.errors;

import lombok.Data;

import java.util.HashMap;

@Data
public class PaymentMethodInvalidError extends Error{

    private HashMap<String, String> errors;

    public PaymentMethodInvalidError(HashMap<String, String> errors) {
        this.errors = errors;
    }
}
