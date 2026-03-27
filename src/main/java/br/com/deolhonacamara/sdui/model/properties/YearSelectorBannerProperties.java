package br.com.deolhonacamara.sdui.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YearSelectorBannerProperties {
    private String title;
    private String subtitle;
    private Integer selectedYear;
    private String buttonBackgroundColor;
}
