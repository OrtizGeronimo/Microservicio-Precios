package precio.precios.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class HistoryDetailDTO {

    private Float amount;
    private LocalDate effectiveDate;
}
