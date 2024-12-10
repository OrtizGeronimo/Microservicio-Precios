package precio.precios.server;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ValidatorService {
    public void validate(@Valid Object data) {
    }
}
