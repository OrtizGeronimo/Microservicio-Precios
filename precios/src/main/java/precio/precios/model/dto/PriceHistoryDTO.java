package precio.precios.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PriceHistoryDTO {

    private Long articleId;
    private List<HistoryDetailDTO> prices;
}
