package br.com.deolhonacamara.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PropositionInput extends Input<PropositionInput>{


    private Integer year;

    protected PropositionInput() {
        super(null);
    }
}
