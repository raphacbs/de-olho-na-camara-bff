package br.com.deolhonacamara.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkDto {
    private String rel;
    private String href;

    public Integer getNumberNextPage() {
        if (rel != null && rel.equals("next")) {
            return getNumberFromHref();
        }
        return null;
    }

    public Integer getNumberLastPage() {
        if (rel != null && rel.equals("last")) {
           return getNumberFromHref();
        }
        return null;
    }

    public String getEndpointNextPage() {
        if (rel != null && rel.equals("next")) {
            return href.split("\\?")[0];
        }
        return null;
    }


    private Integer getNumberFromHref() {
        String[] parts = href.split("pagina=");
        if (parts.length > 1) {
            String pagePart = parts[1].split("&")[0];
            try {
                return Integer.parseInt(pagePart);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }


}
