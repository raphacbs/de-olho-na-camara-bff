package br.com.deolhonacamara.sdui.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropositionDetailHeaderProperties {
    private String title;
    private String presentationDate;
}
