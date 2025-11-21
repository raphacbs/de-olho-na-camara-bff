package br.com.deolhonacamara.api.mapper;


import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.model.*;
import net.coelho.deolhonacamara.api.model.*;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@org.mapstruct.Mapper(componentModel = "spring")
public interface Mapper {
    Mapper INSTANCE = Mappers.getMapper(Mapper.class);

    PoliticianDto toDto(PoliticianEntity e);


    @Mappings({
            @Mapping(source = "documentDate", target = "documentDate", dateFormat = "dd/MM/yyyy"),
            @Mapping(target = "documentValue",
                    expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getDocumentValue()))"),
            @Mapping(target = "netValue",expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getNetValue()))"),
            @Mapping(target = "glosaValue", expression = "java(java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"pt\",\"BR\")).format(e.getGlosaValue()))"),
    })
    ExpenseDto toDto(ExpenseEntity e);

    VoteDto toDto(br.com.deolhonacamara.api.model.VoteEntity e);

    PoliticianVoteDto toDto(PoliticianVoteEntity e);

    @Mappings({
            @Mapping(source = "startDateTime", target = "startDateTime", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "endDateTime", target = "endDateTime", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "startDateTimeEvent", target = "startDateTimeEvent", dateFormat = "dd/MM/yyyy HH:mm"),
            @Mapping(source = "endDateTimeEvent", target = "endDateTimeEvent", dateFormat = "dd/MM/yyyy HH:mm")
    })
    SpeechDto toDto(SpeechEntity e);

    SpeechEntity toDto(SpeechDto o);

    SpeechEntity toDto(Integer politicianId, SpeechBodyDto o);

    PropositionDto toDto(PropositionEntity e);

    PresenceDto toDto(PresenceEntity e);
}
