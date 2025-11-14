package br.com.deolhonacamara.api.mapper;


import br.com.deolhonacamara.api.model.PoliticianEntity;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    PoliticianDto toDto(PoliticianEntity e);
}
