package precio.precios.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PriceWithDealDTO {

    private Long articleId;
    private Long dealId;
    private Float price;
    private Float finalPrice;
}
