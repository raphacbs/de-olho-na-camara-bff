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
public class PoliticianCardProperties {
    private Integer id;
    private String photoUrl;
    private String name;
    private String party;
    private String state;
    private Integer propositionsTotal;
    private Integer expenseTotal;
    private Boolean isFollowed;
    private ComponentAction action;
}
