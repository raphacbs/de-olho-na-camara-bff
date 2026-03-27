package br.com.deolhonacamara.sdui.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PoliticianCardListProperties {
    private List<PoliticianCardProperties> items;
    private Integer total;
    private Integer currentPage;
    private Integer totalPages;
}
