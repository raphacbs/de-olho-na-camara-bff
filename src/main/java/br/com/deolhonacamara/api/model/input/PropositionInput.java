package br.com.deolhonacamara.api.model.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PropositionInput extends Input<PropositionInput>{


    private Integer year;

    public static PropositionInput fromPoliticianQuery(Integer politicianId,
                                                       Integer page,
                                                       Integer size,
                                                       Integer year) {
        return InputBuilder
                .builder(PropositionInput.class)
                .page(page != null ? page : 0)
                .sizePage(size != null ? size : 20)
                .politicianId(politicianId)
                .filter("year", year)
                .build();
    }

    protected PropositionInput() {
        super(null);
    }
}
