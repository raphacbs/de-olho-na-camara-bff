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
public class QuickAccessItem {
    private String id;
    private String icon;
    private String label;
    private ComponentAction action;
}
