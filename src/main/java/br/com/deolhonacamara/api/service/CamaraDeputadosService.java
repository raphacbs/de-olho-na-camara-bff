package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.config.PropertiesConfig;
import br.com.deolhonacamara.api.dto.DeputadosResponseBodyDto;
import br.com.deolhonacamara.request.HTTPShepherd;
import br.com.deolhonacamara.request.repository.HTTPShepherdRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

@Service
public class CamaraDeputadosService {
    private final HTTPShepherd<Void, DeputadosResponseBodyDto> getDeputadosShepherd;

    public CamaraDeputadosService(HttpClient httpClient, Environment environment,
                                  HTTPShepherdRepository httpShepherdRepository, PropertiesConfig config){
        this.getDeputadosShepherd = HTTPShepherd.<Void, DeputadosResponseBodyDto>builder(httpClient, environment, DeputadosResponseBodyDto.class)
                .url(config.getApiCamaraBaseUrl())
                .endpoint("/deputados")
                .timeout(config.getTimeout())
                .contentType("application/json")
                .repository(httpShepherdRepository)
                .build();
    }

    public DeputadosResponseBodyDto getDeputados(){
        return getDeputadosShepherd.request();
    }

}
