package precio.precios.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceDTO {

    private Long articleId;
    private Float amount;
}
