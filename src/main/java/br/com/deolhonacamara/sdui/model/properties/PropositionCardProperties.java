package br.com.deolhonacamara.sdui.model.properties;

import br.com.deolhonacamara.sdui.model.ComponentAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropositionCardProperties {
    private Integer id;
    private String type;
    private Integer number;
    private Integer year;
    private String title;
    private String date;
    private String summary;
    private String appreciation;
    private String typeColor;
    private ComponentAction action;
}
