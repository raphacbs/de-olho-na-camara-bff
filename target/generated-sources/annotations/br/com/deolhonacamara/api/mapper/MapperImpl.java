package br.com.deolhonacamara.api.mapper;

import br.com.deolhonacamara.api.dto.PropositionBodyDto;
import br.com.deolhonacamara.api.dto.SpeechBodyDto;
import br.com.deolhonacamara.api.dto.VoteBodyDto;
import br.com.deolhonacamara.api.dto.VotingBodyByIdDto;
import br.com.deolhonacamara.api.model.ExpenseEntity;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.PoliticianVoteEntity;
import br.com.deolhonacamara.api.model.PresenceEntity;
import br.com.deolhonacamara.api.model.PropositionEntity;
import br.com.deolhonacamara.api.model.SpeechEntity;
import br.com.deolhonacamara.api.model.VoteEntity;
import br.com.deolhonacamara.api.model.VotingEntity;
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
    date = "2026-01-02T15:19:41-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251118-1623, environment: Java 21.0.9 (Eclipse Adoptium)"
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
    public VoteDto toDto(VotingEntity e) {
        if ( e == null ) {
            return null;
        }

        VoteDto voteDto = new VoteDto();

        voteDto.setId( e.getId() );
        voteDto.setDate( e.getDate() );
        voteDto.setDescription( e.getDescription() );

        return voteDto;
    }

    @Override
    public PoliticianVoteDto toDto(PoliticianVoteEntity e) {
        if ( e == null ) {
            return null;
        }

        PoliticianVoteDto politicianVoteDto = new PoliticianVoteDto();

        politicianVoteDto.setVoteOption( e.getVoteType() );
        if ( e.getId() != null ) {
            politicianVoteDto.setId( e.getId().intValue() );
        }
        politicianVoteDto.setVoteId( e.getVoteId() );
        politicianVoteDto.setPoliticianId( e.getPoliticianId() );

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

        speechEntity.audioUrl( o.getAudioUrl() );
        if ( o.getEndDateTime() != null ) {
            speechEntity.endDateTime( LocalDateTime.parse( o.getEndDateTime() ) );
        }
        if ( o.getEndDateTimeEvent() != null ) {
            speechEntity.endDateTimeEvent( LocalDateTime.parse( o.getEndDateTimeEvent() ) );
        }
        speechEntity.eventUri( o.getEventUri() );
        speechEntity.speechType( o.getSpeechType() );
        if ( o.getStartDateTime() != null ) {
            speechEntity.startDateTime( LocalDateTime.parse( o.getStartDateTime() ) );
        }
        if ( o.getStartDateTimeEvent() != null ) {
            speechEntity.startDateTimeEvent( LocalDateTime.parse( o.getStartDateTimeEvent() ) );
        }
        speechEntity.summary( o.getSummary() );
        speechEntity.textUrl( o.getTextUrl() );
        speechEntity.titleEvent( o.getTitleEvent() );
        speechEntity.transcription( o.getTranscription() );
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
            speechEntity.audioUrl( o.getAudioUrl() );
            speechEntity.endDateTime( o.getEndDateTime() );
            speechEntity.endDateTimeEvent( o.getEndDateTimeEvent() );
            speechEntity.eventUri( o.getEventUri() );
            speechEntity.keywords( o.getKeywords() );
            speechEntity.speechType( o.getSpeechType() );
            speechEntity.startDateTime( o.getStartDateTime() );
            speechEntity.startDateTimeEvent( o.getStartDateTimeEvent() );
            speechEntity.summary( o.getSummary() );
            speechEntity.textUrl( o.getTextUrl() );
            speechEntity.titleEvent( o.getTitleEvent() );
            speechEntity.transcription( o.getTranscription() );
            speechEntity.videoUrl( o.getVideoUrl() );
        }
        speechEntity.politicianId( politicianId );

        return speechEntity.build();
    }

    @Override
    public VotingEntity toEntity(VotingBodyByIdDto dto) {
        if ( dto == null ) {
            return null;
        }

        VotingEntity.VotingEntityBuilder votingEntity = VotingEntity.builder();

        votingEntity.registeredEffects( registeredEffectsToJson( dto.getRegisteredEffects() ) );
        votingEntity.possibleObjects( possibleObjectsToJson( dto.getPossibleObjects() ) );
        votingEntity.affectedPropositions( affectedPropositionsToJson( dto.getAffectedPropositions() ) );
        votingEntity.lastPropositionPresentation( lastPropositionPresentationToJson( dto.getLastPropositionPresentation() ) );
        votingEntity.approval( dto.getApproval() );
        if ( dto.getDate() != null ) {
            votingEntity.date( LocalDate.parse( dto.getDate() ) );
        }
        votingEntity.description( dto.getDescription() );
        votingEntity.eventId( dto.getEventId() );
        votingEntity.eventUri( dto.getEventUri() );
        votingEntity.id( dto.getId() );
        if ( dto.getLastVotingOpenDateTime() != null ) {
            votingEntity.lastVotingOpenDateTime( LocalDateTime.parse( dto.getLastVotingOpenDateTime() ) );
        }
        votingEntity.lastVotingOpenDescription( dto.getLastVotingOpenDescription() );
        votingEntity.organAcronym( dto.getOrganAcronym() );
        votingEntity.organId( dto.getOrganId() );
        votingEntity.organUri( dto.getOrganUri() );
        if ( dto.getRegistrationDateTime() != null ) {
            votingEntity.registrationDateTime( LocalDateTime.parse( dto.getRegistrationDateTime() ) );
        }
        votingEntity.uri( dto.getUri() );

        return votingEntity.build();
    }

    @Override
    public VoteEntity toEntity(VoteBodyDto dto, String voteId, String votingId) {
        if ( dto == null && voteId == null && votingId == null ) {
            return null;
        }

        VoteEntity.VoteEntityBuilder voteEntity = VoteEntity.builder();

        if ( dto != null ) {
            voteEntity.voteRegistrationDate( dto.getDataRegistroVoto() );
            voteEntity.politicianId( dtoDeputadoId( dto ) );
            voteEntity.voteType( dto.getTipoVoto() );
        }
        voteEntity.voteId( voteId );
        voteEntity.votingId( votingId );

        return voteEntity.build();
    }

    @Override
    public PoliticianVoteEntity toPoliticianVoteEntity(VoteBodyDto dto, String voteId) {
        if ( dto == null && voteId == null ) {
            return null;
        }

        PoliticianVoteEntity.PoliticianVoteEntityBuilder politicianVoteEntity = PoliticianVoteEntity.builder();

        if ( dto != null ) {
            politicianVoteEntity.voteRegistrationDate( dto.getDataRegistroVoto() );
            politicianVoteEntity.politicianId( dtoDeputadoId( dto ) );
            politicianVoteEntity.voteType( dto.getTipoVoto() );
        }
        politicianVoteEntity.voteId( voteId );

        return politicianVoteEntity.build();
    }

    @Override
    public PropositionDto toDto(PropositionEntity e) {
        if ( e == null ) {
            return null;
        }

        PropositionDto propositionDto = new PropositionDto();

        propositionDto.setId( e.getId() );
        propositionDto.setUri( e.getUri() );
        propositionDto.setType( e.getType() );
        propositionDto.setCodeType( e.getCodeType() );
        propositionDto.setNumber( e.getNumber() );
        propositionDto.setYear( e.getYear() );
        propositionDto.setSummary( e.getSummary() );
        propositionDto.setDetailedSummary( e.getDetailedSummary() );
        propositionDto.setPresentationDate( localDateTimeToLocalDate( e.getPresentationDate() ) );
        propositionDto.setStatusDateTime( localDateTimeToString( e.getStatusDateTime() ) );
        propositionDto.setStatusLastReporterUri( e.getStatusLastReporterUri() );
        propositionDto.setStatusTramitationDescription( e.getStatusTramitationDescription() );
        propositionDto.setStatusTramitationTypeCode( e.getStatusTramitationTypeCode() );
        propositionDto.setStatusSituationDescription( e.getStatusSituationDescription() );
        propositionDto.setStatusSituationCode( e.getStatusSituationCode() );
        propositionDto.setStatusDispatch( e.getStatusDispatch() );
        propositionDto.setStatusUrl( e.getStatusUrl() );
        propositionDto.setStatusScope( e.getStatusScope() );
        propositionDto.setStatusAppreciation( e.getStatusAppreciation() );
        propositionDto.setUriOrgaoNumerador( e.getUriOrgaoNumerador() );
        propositionDto.setUriAutores( e.getUriAutores() );
        propositionDto.setTypeDescription( e.getTypeDescription() );
        propositionDto.setKeywords( e.getKeywords() );
        propositionDto.setUriPropPrincipal( e.getUriPropPrincipal() );
        propositionDto.setUriPropAnterior( e.getUriPropAnterior() );
        propositionDto.setUriPropPosterior( e.getUriPropPosterior() );
        propositionDto.setUrlInteiroTeor( e.getUrlInteiroTeor() );
        propositionDto.setUrnFinal( e.getUrnFinal() );
        propositionDto.setText( e.getText() );
        propositionDto.setJustification( e.getJustification() );
        propositionDto.setCreatedAt( localDateTimeToString( e.getCreatedAt() ) );
        propositionDto.setUpdatedAt( localDateTimeToString( e.getUpdatedAt() ) );

        propositionDto.setStatus( statusFromEntity(e) );

        return propositionDto;
    }

    @Override
    public PropositionEntity toEntity(PropositionDto d) {
        if ( d == null ) {
            return null;
        }

        PropositionEntity.PropositionEntityBuilder propositionEntity = PropositionEntity.builder();

        propositionEntity.id( d.getId() );
        propositionEntity.uri( d.getUri() );
        propositionEntity.type( d.getType() );
        propositionEntity.codeType( d.getCodeType() );
        propositionEntity.number( d.getNumber() );
        propositionEntity.year( d.getYear() );
        propositionEntity.summary( d.getSummary() );
        propositionEntity.detailedSummary( d.getDetailedSummary() );
        propositionEntity.presentationDate( localDateToLocalDateTime( d.getPresentationDate() ) );
        propositionEntity.statusDateTime( stringToLocalDateTime( d.getStatusDateTime() ) );
        propositionEntity.statusLastReporterUri( d.getStatusLastReporterUri() );
        propositionEntity.statusTramitationDescription( d.getStatusTramitationDescription() );
        propositionEntity.statusTramitationTypeCode( d.getStatusTramitationTypeCode() );
        propositionEntity.statusSituationDescription( d.getStatusSituationDescription() );
        propositionEntity.statusSituationCode( d.getStatusSituationCode() );
        propositionEntity.statusDispatch( d.getStatusDispatch() );
        propositionEntity.statusUrl( d.getStatusUrl() );
        propositionEntity.statusScope( d.getStatusScope() );
        propositionEntity.statusAppreciation( d.getStatusAppreciation() );
        propositionEntity.uriOrgaoNumerador( d.getUriOrgaoNumerador() );
        propositionEntity.uriAutores( d.getUriAutores() );
        propositionEntity.typeDescription( d.getTypeDescription() );
        propositionEntity.keywords( d.getKeywords() );
        propositionEntity.uriPropPrincipal( d.getUriPropPrincipal() );
        propositionEntity.uriPropAnterior( d.getUriPropAnterior() );
        propositionEntity.uriPropPosterior( d.getUriPropPosterior() );
        propositionEntity.urlInteiroTeor( d.getUrlInteiroTeor() );
        propositionEntity.urnFinal( d.getUrnFinal() );
        propositionEntity.text( d.getText() );
        propositionEntity.justification( d.getJustification() );
        propositionEntity.createdAt( stringToLocalDateTime( d.getCreatedAt() ) );
        propositionEntity.updatedAt( stringToLocalDateTime( d.getUpdatedAt() ) );

        return propositionEntity.build();
    }

    @Override
    public PropositionEntity toEntity(PropositionBodyDto d) {
        if ( d == null ) {
            return null;
        }

        PropositionEntity.PropositionEntityBuilder propositionEntity = PropositionEntity.builder();

        propositionEntity.id( d.getId() );
        propositionEntity.uri( d.getUri() );
        propositionEntity.type( d.getType() );
        propositionEntity.codeType( d.getCodeType() );
        propositionEntity.number( d.getNumber() );
        propositionEntity.year( d.getYear() );
        propositionEntity.summary( d.getSummary() );
        propositionEntity.detailedSummary( d.getDetailedSummary() );
        propositionEntity.presentationDate( d.getPresentationDate() );
        propositionEntity.statusDateTime( dStatusPropositionDateTime( d ) );
        propositionEntity.statusLastReporterUri( dStatusPropositionLastReporterUri( d ) );
        propositionEntity.statusTramitationDescription( dStatusPropositionTramitationDescription( d ) );
        propositionEntity.statusTramitationTypeCode( dStatusPropositionTramitationTypeCode( d ) );
        propositionEntity.statusSituationDescription( dStatusPropositionSituationDescription( d ) );
        propositionEntity.statusSituationCode( dStatusPropositionSituationCode( d ) );
        propositionEntity.statusDispatch( dStatusPropositionDispatch( d ) );
        propositionEntity.statusUrl( dStatusPropositionUrl( d ) );
        propositionEntity.statusScope( dStatusPropositionScope( d ) );
        propositionEntity.statusAppreciation( dStatusPropositionAppreciation( d ) );
        propositionEntity.uriOrgaoNumerador( d.getUriOrgaoNumerador() );
        propositionEntity.uriAutores( d.getUriAutores() );
        propositionEntity.typeDescription( d.getTypeDescription() );
        propositionEntity.keywords( d.getKeywords() );
        propositionEntity.uriPropPrincipal( d.getUriPropPrincipal() );
        propositionEntity.uriPropAnterior( d.getUriPropAnterior() );
        propositionEntity.uriPropPosterior( d.getUriPropPosterior() );
        propositionEntity.urlInteiroTeor( d.getUrlInteiroTeor() );
        propositionEntity.urnFinal( d.getUrnFinal() );
        propositionEntity.text( d.getText() );
        propositionEntity.justification( d.getJustification() );

        propositionEntity.createdAt( java.time.LocalDateTime.now() );
        propositionEntity.updatedAt( java.time.LocalDateTime.now() );

        return propositionEntity.build();
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

    @Override
    public PropositionEntity fromBody(PropositionBodyDto b) {
        if ( b == null ) {
            return null;
        }

        PropositionEntity.PropositionEntityBuilder propositionEntity = PropositionEntity.builder();

        propositionEntity.id( b.getId() );
        propositionEntity.uri( b.getUri() );
        propositionEntity.type( b.getType() );
        propositionEntity.codeType( b.getCodeType() );
        propositionEntity.number( b.getNumber() );
        propositionEntity.year( b.getYear() );
        propositionEntity.summary( b.getSummary() );
        propositionEntity.presentationDate( b.getPresentationDate() );
        propositionEntity.uriOrgaoNumerador( b.getUriOrgaoNumerador() );
        propositionEntity.uriAutores( b.getUriAutores() );
        propositionEntity.typeDescription( b.getTypeDescription() );
        propositionEntity.detailedSummary( b.getDetailedSummary() );
        propositionEntity.keywords( b.getKeywords() );
        propositionEntity.uriPropPrincipal( b.getUriPropPrincipal() );
        propositionEntity.uriPropAnterior( b.getUriPropAnterior() );
        propositionEntity.uriPropPosterior( b.getUriPropPosterior() );
        propositionEntity.urlInteiroTeor( b.getUrlInteiroTeor() );
        propositionEntity.urnFinal( b.getUrnFinal() );
        propositionEntity.text( b.getText() );
        propositionEntity.justification( b.getJustification() );
        propositionEntity.statusDateTime( dStatusPropositionDateTime( b ) );
        propositionEntity.statusLastReporterUri( dStatusPropositionLastReporterUri( b ) );
        propositionEntity.statusTramitationDescription( dStatusPropositionTramitationDescription( b ) );
        propositionEntity.statusTramitationTypeCode( dStatusPropositionTramitationTypeCode( b ) );
        propositionEntity.statusSituationDescription( dStatusPropositionSituationDescription( b ) );
        propositionEntity.statusSituationCode( dStatusPropositionSituationCode( b ) );
        propositionEntity.statusDispatch( dStatusPropositionDispatch( b ) );
        propositionEntity.statusUrl( dStatusPropositionUrl( b ) );
        propositionEntity.statusScope( dStatusPropositionScope( b ) );
        propositionEntity.statusAppreciation( dStatusPropositionAppreciation( b ) );

        return propositionEntity.build();
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

    private Integer dtoDeputadoId(VoteBodyDto voteBodyDto) {
        if ( voteBodyDto == null ) {
            return null;
        }
        VoteBodyDto.Deputado deputado = voteBodyDto.getDeputado();
        if ( deputado == null ) {
            return null;
        }
        Integer id = deputado.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private LocalDateTime dStatusPropositionDateTime(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        LocalDateTime dateTime = statusProposition.getDateTime();
        if ( dateTime == null ) {
            return null;
        }
        return dateTime;
    }

    private String dStatusPropositionLastReporterUri(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String lastReporterUri = statusProposition.getLastReporterUri();
        if ( lastReporterUri == null ) {
            return null;
        }
        return lastReporterUri;
    }

    private String dStatusPropositionTramitationDescription(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String tramitationDescription = statusProposition.getTramitationDescription();
        if ( tramitationDescription == null ) {
            return null;
        }
        return tramitationDescription;
    }

    private String dStatusPropositionTramitationTypeCode(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String tramitationTypeCode = statusProposition.getTramitationTypeCode();
        if ( tramitationTypeCode == null ) {
            return null;
        }
        return tramitationTypeCode;
    }

    private String dStatusPropositionSituationDescription(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String situationDescription = statusProposition.getSituationDescription();
        if ( situationDescription == null ) {
            return null;
        }
        return situationDescription;
    }

    private String dStatusPropositionSituationCode(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String situationCode = statusProposition.getSituationCode();
        if ( situationCode == null ) {
            return null;
        }
        return situationCode;
    }

    private String dStatusPropositionDispatch(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String dispatch = statusProposition.getDispatch();
        if ( dispatch == null ) {
            return null;
        }
        return dispatch;
    }

    private String dStatusPropositionUrl(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String url = statusProposition.getUrl();
        if ( url == null ) {
            return null;
        }
        return url;
    }

    private String dStatusPropositionScope(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String scope = statusProposition.getScope();
        if ( scope == null ) {
            return null;
        }
        return scope;
    }

    private String dStatusPropositionAppreciation(PropositionBodyDto propositionBodyDto) {
        if ( propositionBodyDto == null ) {
            return null;
        }
        PropositionBodyDto.PropositionStatusDto statusProposition = propositionBodyDto.getStatusProposition();
        if ( statusProposition == null ) {
            return null;
        }
        String appreciation = statusProposition.getAppreciation();
        if ( appreciation == null ) {
            return null;
        }
        return appreciation;
    }
}
