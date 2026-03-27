package br.com.deolhonacamara.sdui.model.properties;

import br.com.deolhonacamara.sdui.model.ComponentAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatCardItem {
    private String id;
    private String icon;
    private String value;
    private String label;
    private String backgroundColor;
    private ComponentAction action;
}
