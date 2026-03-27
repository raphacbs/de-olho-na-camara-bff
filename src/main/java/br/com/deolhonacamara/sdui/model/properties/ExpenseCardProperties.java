package br.com.deolhonacamara.sdui.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCardProperties {
    private Integer id;
    private String expenseType;
    private String supplier;
    private String amount;
    private String date;
    private String documentUrl;
}
