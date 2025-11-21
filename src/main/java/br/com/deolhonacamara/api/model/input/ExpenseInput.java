package br.com.deolhonacamara.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ExpenseInput extends Input<ExpenseInput>{
    private Integer year;
    private Integer month;
    private Integer politicianId;

    protected ExpenseInput() {
        super(null);
    }
}
