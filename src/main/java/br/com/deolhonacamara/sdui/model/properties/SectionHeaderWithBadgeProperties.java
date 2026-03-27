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
public class SectionHeaderWithBadgeProperties {
    private String title;
    private Integer badgeCount;
    private String badgeBackgroundColor;
    private ComponentAction action;
}
