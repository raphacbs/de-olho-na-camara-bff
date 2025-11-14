package br.com.deolhonacamara.api.mapper;

import br.com.deolhonacamara.api.model.PoliticianEntity;
import javax.annotation.processing.Generated;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T18:40:19-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class MapperImpl implements Mapper {

    @Override
    public PoliticianDto toDto(PoliticianEntity e) {
        if ( e == null ) {
            return null;
        }

        PoliticianDto politicianDto = new PoliticianDto();

        politicianDto.setId( e.getId() );
        politicianDto.setName( e.getName() );
        politicianDto.setParty( e.getParty() );
        politicianDto.setPartyUri( e.getPartyUri() );
        politicianDto.setState( e.getState() );
        politicianDto.setLegislatureId( e.getLegislatureId() );
        politicianDto.setEmail( e.getEmail() );
        politicianDto.setUri( e.getUri() );
        politicianDto.setPhotoUrl( e.getPhotoUrl() );

        return politicianDto;
    }
}
