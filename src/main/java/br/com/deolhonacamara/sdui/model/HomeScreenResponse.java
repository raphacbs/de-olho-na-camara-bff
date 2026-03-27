package br.com.deolhonacamara.sdui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeScreenResponse {
    private String screenId;
    private String version;
    private List<ScreenComponent> components;
}
