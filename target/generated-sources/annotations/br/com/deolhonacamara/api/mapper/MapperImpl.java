package br.com.deolhonacamara.api.mapper;

import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
import br.com.deolhonacamara.api.model.PresenceEntity;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.model.VoteEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import javax.annotation.processing.Generated;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import net.coelho.deolhonacamara.api.model.ExpenseDto;
import net.coelho.deolhonacamara.api.model.PoliticianDto;
import net.coelho.deolhonacamara.api.model.PoliticianVoteDto;
import net.coelho.deolhonacamara.api.model.PresenceDto;
import net.coelho.deolhonacamara.api.model.PropositionDto;
import net.coelho.deolhonacamara.api.model.SpeechDto;
import net.coelho.deolhonacamara.api.model.VoteDto;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-21T10:55:04-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Amazon.com Inc.)"
)
@Component
public class MapperImpl implements Mapper {

    private final DateTimeFormatter dateTimeFormatter_dd_MM_yyyy_HH_mm_0230740742 = DateTimeFormatter.ofPattern( "dd/MM/yyyy HH:mm" );
    private final DatatypeFactory datatypeFactory;

    public MapperImpl() {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        }
        catch ( DatatypeConfigurationException ex ) {
            throw new RuntimeException( ex );
        }
    }

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

    @Override
    public ExpenseDto toDto(ExpenseEntity e) {
        if ( e == null ) {
            return null;
        }

        ExpenseDto expenseDto = new ExpenseDto();

        expenseDto.setDocumentDate( xmlGregorianCalendarToLocalDate( localDateTimeToXmlGregorianCalendar( e.getDocumentDate() ) ) );
        expenseDto.setId( e.getId() );
        expenseDto.setPoliticianId( e.getPoliticianId() );
        expenseDto.setYear( e.getYear() );
        expenseDto.setMonth( e.getMonth() );
        expenseDto.setExpenseType( e.getExpenseType() );
        expenseDto.setSupplier( e.getSupplier() );
        expenseDto.setDocumentUrl( e.getDocumentUrl() );

        expenseDto.setDocumentValue( java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt","BR")).format(e.getDocumentValue()) );
        expenseDto.setNetValue( java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt","BR")).format(e.getNetValue()) );
        expenseDto.setGlosaValue( java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("pt","BR")).format(e.getGlosaValue()) );

        return expenseDto;
    }

    @Override
    public VoteDto toDto(VoteEntity e) {
        if ( e == null ) {
            return null;
        }

        VoteDto voteDto = new VoteDto();

        voteDto.setId( e.getId() );
        voteDto.setDate( e.getDate() );
        voteDto.setDescription( e.getDescription() );
        voteDto.setSummary( e.getSummary() );

        return voteDto;
    }

    @Override
    public PoliticianVoteDto toDto(PoliticianVoteEntity e) {
        if ( e == null ) {
            return null;
        }

        PoliticianVoteDto politicianVoteDto = new PoliticianVoteDto();

        politicianVoteDto.setId( e.getId() );
        politicianVoteDto.setVoteId( e.getVoteId() );
        politicianVoteDto.setPoliticianId( e.getPoliticianId() );
        politicianVoteDto.setVoteOption( e.getVoteOption() );
        politicianVoteDto.setVote( toDto( e.getVote() ) );

        return politicianVoteDto;
    }

    @Override
    public SpeechDto toDto(SpeechEntity e) {
        if ( e == null ) {
            return null;
        }

        SpeechDto speechDto = new SpeechDto();

        if ( e.getStartDateTime() != null ) {
            speechDto.setStartDateTime( dateTimeFormatter_dd_MM_yyyy_HH_mm_0230740742.format( e.getStartDateTime() ) );
        }
        if ( e.getEndDateTime() != null ) {
            speechDto.setEndDateTime( dateTimeFormatter_dd_MM_yyyy_HH_mm_0230740742.format( e.getEndDateTime() ) );
        }
        if ( e.getStartDateTimeEvent() != null ) {
            speechDto.setStartDateTimeEvent( dateTimeFormatter_dd_MM_yyyy_HH_mm_0230740742.format( e.getStartDateTimeEvent() ) );
        }
        if ( e.getEndDateTimeEvent() != null ) {
            speechDto.setEndDateTimeEvent( dateTimeFormatter_dd_MM_yyyy_HH_mm_0230740742.format( e.getEndDateTimeEvent() ) );
        }
        speechDto.setTitleEvent( e.getTitleEvent() );
        speechDto.setSummary( e.getSummary() );
        speechDto.setSpeechType( e.getSpeechType() );
        speechDto.setTranscription( e.getTranscription() );
        speechDto.setEventUri( e.getEventUri() );
        speechDto.setAudioUrl( e.getAudioUrl() );
        speechDto.setTextUrl( e.getTextUrl() );
        speechDto.setVideoUrl( e.getVideoUrl() );

        return speechDto;
    }

    @Override
    public SpeechEntity toDto(SpeechDto o) {
        if ( o == null ) {
            return null;
        }

        SpeechEntity.SpeechEntityBuilder speechEntity = SpeechEntity.builder();

        if ( o.getStartDateTime() != null ) {
            speechEntity.startDateTime( LocalDateTime.parse( o.getStartDateTime() ) );
        }
        if ( o.getEndDateTime() != null ) {
            speechEntity.endDateTime( LocalDateTime.parse( o.getEndDateTime() ) );
        }
        speechEntity.titleEvent( o.getTitleEvent() );
        if ( o.getStartDateTimeEvent() != null ) {
            speechEntity.startDateTimeEvent( LocalDateTime.parse( o.getStartDateTimeEvent() ) );
        }
        if ( o.getEndDateTimeEvent() != null ) {
            speechEntity.endDateTimeEvent( LocalDateTime.parse( o.getEndDateTimeEvent() ) );
        }
        speechEntity.summary( o.getSummary() );
        speechEntity.speechType( o.getSpeechType() );
        speechEntity.transcription( o.getTranscription() );
        speechEntity.eventUri( o.getEventUri() );
        speechEntity.audioUrl( o.getAudioUrl() );
        speechEntity.textUrl( o.getTextUrl() );
        speechEntity.videoUrl( o.getVideoUrl() );

        return speechEntity.build();
    }

    @Override
    public SpeechEntity toDto(Integer politicianId, SpeechBodyDto o) {
        if ( politicianId == null && o == null ) {
            return null;
        }

        SpeechEntity.SpeechEntityBuilder speechEntity = SpeechEntity.builder();

        if ( o != null ) {
            speechEntity.startDateTime( o.getStartDateTime() );
            speechEntity.endDateTime( o.getEndDateTime() );
            speechEntity.titleEvent( o.getTitleEvent() );
            speechEntity.startDateTimeEvent( o.getStartDateTimeEvent() );
            speechEntity.endDateTimeEvent( o.getEndDateTimeEvent() );
            speechEntity.keywords( o.getKeywords() );
            speechEntity.summary( o.getSummary() );
            speechEntity.speechType( o.getSpeechType() );
            speechEntity.transcription( o.getTranscription() );
            speechEntity.eventUri( o.getEventUri() );
            speechEntity.audioUrl( o.getAudioUrl() );
            speechEntity.textUrl( o.getTextUrl() );
            speechEntity.videoUrl( o.getVideoUrl() );
        }
        speechEntity.politicianId( politicianId );

        return speechEntity.build();
    }

    @Override
    public PropositionDto toDto(PropositionEntity e) {
        if ( e == null ) {
            return null;
        }

        PropositionDto propositionDto = new PropositionDto();

        propositionDto.setId( e.getId() );
        propositionDto.setType( e.getType() );
        propositionDto.setNumber( e.getNumber() );
        propositionDto.setYear( e.getYear() );
        propositionDto.setSummary( e.getSummary() );
        propositionDto.setPresentationDate( e.getPresentationDate() );
        propositionDto.setStatus( e.getStatus() );

        return propositionDto;
    }

    @Override
    public PresenceDto toDto(PresenceEntity e) {
        if ( e == null ) {
            return null;
        }

        PresenceDto presenceDto = new PresenceDto();

        presenceDto.setId( e.getId() );
        presenceDto.setPoliticianId( e.getPoliticianId() );
        presenceDto.setDate( e.getDate() );
        presenceDto.setDescription( e.getDescription() );
        presenceDto.setStatus( e.getStatus() );

        return presenceDto;
    }

    private XMLGregorianCalendar localDateTimeToXmlGregorianCalendar( LocalDateTime localDateTime ) {
        if ( localDateTime == null ) {
            return null;
        }

        return datatypeFactory.newXMLGregorianCalendar(
            localDateTime.getYear(),
            localDateTime.getMonthValue(),
            localDateTime.getDayOfMonth(),
            localDateTime.getHour(),
            localDateTime.getMinute(),
            localDateTime.getSecond(),
            localDateTime.get( ChronoField.MILLI_OF_SECOND ),
            DatatypeConstants.FIELD_UNDEFINED );
    }

    private static LocalDate xmlGregorianCalendarToLocalDate( XMLGregorianCalendar xcal ) {
        if ( xcal == null ) {
            return null;
        }

        return LocalDate.of( xcal.getYear(), xcal.getMonth(), xcal.getDay() );
    }
}
