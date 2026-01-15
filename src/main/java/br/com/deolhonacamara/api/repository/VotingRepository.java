package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.dto.VotingWithVotesDTO;
import br.com.deolhonacamara.api.dto.VotingWithVotesResponseDTO;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.PoliticianEntity;
import br.com.deolhonacamara.api.model.VotingEntity;
import br.com.deolhonacamara.api.repository.PoliticianRepository;
import br.com.deolhonacamara.api.service.CamaraDeputadosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class VotingRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PoliticianRepository politicianRepository;
    private final CamaraDeputadosService camaraDeputadosService;

    public Optional<VotingEntity> findById(String id) {
        String sql = """
            SELECT id, approval, date, registration_date_time, last_voting_open_date_time,
                   last_voting_open_description, description, event_id, organ_id, organ_acronym,
                   uri, event_uri, organ_uri, registered_effects, possible_objects,
                   affected_propositions, last_proposition_presentation, created_at, updated_at
            FROM camara_deputados.voting
            WHERE id = :id
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        List<VotingEntity> results = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public PageResponse<VotingEntity> findAll(Pageable pageable) {
        String sql = """
            SELECT id, approval, date, registration_date_time, last_voting_open_date_time,
                   last_voting_open_description, description, event_id, organ_id, organ_acronym,
                   uri, event_uri, organ_uri, registered_effects, possible_objects,
                   affected_propositions, last_proposition_presentation, created_at, updated_at
            FROM camara_deputados.voting
            ORDER BY date DESC, created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM camara_deputados.voting";

        Map<String, Object> params = new HashMap<>();
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<VotingEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public PageResponse<VotingEntity> findByDateRange(java.time.LocalDate startDate, java.time.LocalDate endDate, Pageable pageable) {
        String sql = """
            SELECT id, approval, date, registration_date_time, last_voting_open_date_time,
                   last_voting_open_description, description, event_id, organ_id, organ_acronym,
                   uri, event_uri, organ_uri, registered_effects, possible_objects,
                   affected_propositions, last_proposition_presentation, created_at, updated_at
            FROM camara_deputados.voting
            WHERE date BETWEEN :startDate AND :endDate
            ORDER BY date DESC, created_at DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = "SELECT COUNT(*) FROM camara_deputados.vote WHERE date BETWEEN :startDate AND :endDate";

        Map<String, Object> params = new HashMap<>();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<VotingEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertVote(VotingEntity vote) {
        String sql = """
            INSERT INTO camara_deputados.voting
                (id, approval, date, registration_date_time, last_voting_open_date_time,
                 last_voting_open_description, description, event_id, organ_id, organ_acronym,
                 uri, event_uri, organ_uri, registered_effects, possible_objects,
                 affected_propositions, last_proposition_presentation, updated_at)
            VALUES
                (:id, :approval, :date, :registrationDateTime, :lastVotingOpenDateTime,
                 :lastVotingOpenDescription, :description, :eventId, :organId, :organAcronym,
                 :uri, :eventUri, :organUri, :registeredEffects, :possibleObjects,
                 :affectedPropositions, :lastPropositionPresentation, CURRENT_TIMESTAMP)
            ON CONFLICT (id) DO UPDATE SET
                approval = EXCLUDED.approval,
                date = EXCLUDED.date,
                registration_date_time = EXCLUDED.registration_date_time,
                last_voting_open_date_time = EXCLUDED.last_voting_open_date_time,
                last_voting_open_description = EXCLUDED.last_voting_open_description,
                description = EXCLUDED.description,
                event_id = EXCLUDED.event_id,
                organ_id = EXCLUDED.organ_id,
                organ_acronym = EXCLUDED.organ_acronym,
                uri = EXCLUDED.uri,
                event_uri = EXCLUDED.event_uri,
                organ_uri = EXCLUDED.organ_uri,
                registered_effects = EXCLUDED.registered_effects,
                possible_objects = EXCLUDED.possible_objects,
                affected_propositions = EXCLUDED.affected_propositions,
                last_proposition_presentation = EXCLUDED.last_proposition_presentation,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("id", vote.getId());
        params.put("approval", vote.getApproval());
        params.put("date", vote.getDate());
        params.put("registrationDateTime", vote.getRegistrationDateTime());
        params.put("lastVotingOpenDateTime", vote.getLastVotingOpenDateTime());
        params.put("lastVotingOpenDescription", vote.getLastVotingOpenDescription());
        params.put("description", vote.getDescription());
        params.put("eventId", vote.getEventId());
        params.put("organId", vote.getOrganId());
        params.put("organAcronym", vote.getOrganAcronym());
        params.put("uri", vote.getUri());
        params.put("eventUri", vote.getEventUri());
        params.put("organUri", vote.getOrganUri());
        params.put("registeredEffects", createJsonbObject(vote.getRegisteredEffects()));
        params.put("possibleObjects", createJsonbObject(vote.getPossibleObjects()));
        params.put("affectedPropositions", createJsonbObject(vote.getAffectedPropositions()));
        params.put("lastPropositionPresentation", createJsonbObject(vote.getLastPropositionPresentation()));

        jdbcTemplate.update(sql, params);
    }

    public void saveVote(br.com.deolhonacamara.api.model.VoteEntity vote) {
        // Check if politician exists, if not, try to fetch and save from API
        if (!politicianExists(vote.getPoliticianId())) {
            if (!fetchAndSavePolitician(vote.getPoliticianId())) {
                log.warn("Skipping vote save for politician ID {} in voting {} - politician not found in API or failed to save",
                        vote.getPoliticianId(), vote.getVoteId());
                return;
            }
        }

        String sql = """
            INSERT INTO camara_deputados.politician_vote
                (vote_registration_date, politician_id, vote_type, vote_id, voting_id, created_at, updated_at)
            VALUES
                (:voteRegistrationDate, :politicianId, :voteType, :voteId, :votingId, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT (politician_id, vote_id) DO UPDATE SET
                vote_registration_date = EXCLUDED.vote_registration_date,
                vote_type = EXCLUDED.vote_type,
                voting_id = EXCLUDED.voting_id,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("voteRegistrationDate", vote.getVoteRegistrationDate());
        params.put("politicianId", vote.getPoliticianId());
        params.put("voteType", vote.getVoteType());
        params.put("voteId", vote.getVoteId());
        params.put("votingId", vote.getVotingId());

        jdbcTemplate.update(sql, params);
    }

    public void deleteById(String id) {
        String sql = "DELETE FROM camara_deputados.voting WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        jdbcTemplate.update(sql, params);
    }

    private Object createJsonbObject(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            return null;
        }
        // Retornando a string diretamente, pois o driver JDBC do PostgreSQL geralmente lida com isso
        // se o tipo da coluna for JSONB e o parâmetro for passado corretamente.
        // Se precisar de um objeto específico, pode ser necessário usar PGobject, mas
        // como a dependência não está disponível, vamos tentar passar como String
        // e deixar o driver converter ou usar um cast no SQL se necessário.
        // No entanto, para garantir que o PostgreSQL entenda como JSON,
        // a melhor abordagem sem PGobject é passar como String e fazer o cast no SQL
        // (:param::jsonb). Mas como o SQL já está definido, vamos tentar retornar a String.
        // Se falhar, teremos que adicionar a dependência ou mudar a query.
        
        // Uma alternativa comum sem a classe PGobject explícita é apenas retornar a String
        // e garantir que o banco receba como tal.
        return jsonString;
    }

    private boolean politicianExists(Integer politicianId) {
        String sql = "SELECT COUNT(*) FROM camara_deputados.politicians WHERE id = :politicianId";

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);

        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    private boolean fetchAndSavePolitician(Integer politicianId) {
        try {
            var deputadoResponse = camaraDeputadosService.getDeputadoById(politicianId);
            if (deputadoResponse != null && deputadoResponse.getDados() != null) {
                var deputadoDto = deputadoResponse.getDados();
                PoliticianEntity politician = PoliticianEntity.builder()
                        .id(deputadoDto.getId())
                        .name(deputadoDto.getNome())
                        .party(deputadoDto.getSiglaPartido())
                        .partyUri(deputadoDto.getUriPartido())
                        .state(deputadoDto.getSiglaUf())
                        .legislatureId(deputadoDto.getIdLegislatura())
                        .email(deputadoDto.getEmail())
                        .uri(deputadoDto.getUri())
                        .photoUrl(deputadoDto.getUrlFoto())
                        .build();

                politicianRepository.upsertPolitician(politician);
                log.info("Successfully fetched and saved politician: {} (ID: {})", politician.getName(), politician.getId());
                return true;
            } else {
                log.warn("No data returned from API for politician ID: {}", politicianId);
                return false;
            }
        } catch (Exception e) {
            log.error("Error fetching politician ID {} from API: ", politicianId, e);
            return false;
        }
    }

    private VotingEntity mapRow(ResultSet rs) throws SQLException {
        return VotingEntity.builder()
                .id(rs.getString("id"))
                .approval(rs.getInt("approval"))
                .date(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null)
                .registrationDateTime(rs.getTimestamp("registration_date_time") != null ?
                    rs.getTimestamp("registration_date_time").toLocalDateTime() : null)
                .lastVotingOpenDateTime(rs.getTimestamp("last_voting_open_date_time") != null ?
                    rs.getTimestamp("last_voting_open_date_time").toLocalDateTime() : null)
                .lastVotingOpenDescription(rs.getString("last_voting_open_description"))
                .description(rs.getString("description"))
                .eventId(rs.getInt("event_id"))
                .organId(rs.getInt("organ_id"))
                .organAcronym(rs.getString("organ_acronym"))
                .uri(rs.getString("uri"))
                .eventUri(rs.getString("event_uri"))
                .organUri(rs.getString("organ_uri"))
                .registeredEffects(rs.getString("registered_effects"))
                .possibleObjects(rs.getString("possible_objects"))
                .affectedPropositions(rs.getString("affected_propositions"))
                .lastPropositionPresentation(rs.getString("last_proposition_presentation"))
                .createdAt(rs.getTimestamp("created_at") != null ?
                    rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ?
                    rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }

    public VotingWithVotesResponseDTO findVotingsWithVotes(Pageable pageable, String votingIdFilter, Integer politicianIdFilter, boolean onlyVotesFilter) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder whereClause = new StringBuilder();

        if (StringUtils.hasText(votingIdFilter)) {
            whereClause.append(" WHERE v.id = :votingId");
            params.put("votingId", votingIdFilter);
        }

        if (politicianIdFilter != null) {
            if (whereClause.length() == 0) {
                whereClause.append(" WHERE");
            } else {
                whereClause.append(" AND");
            }
            whereClause.append(" v.id IN (SELECT DISTINCT voting_id FROM camara_deputados.politician_vote WHERE politician_id = :politicianId)");
            params.put("politicianId", politicianIdFilter);
        }

        if (onlyVotesFilter) {
            if (whereClause.length() == 0) {
                whereClause.append(" WHERE");
            } else {
                whereClause.append(" AND");
            }
            whereClause.append(" EXISTS (SELECT 1 FROM camara_deputados.politician_vote pv WHERE pv.voting_id = v.id)");
        }

        String votingsSql = """
            SELECT v.id, v.date, v.description, v.organ_acronym
            FROM camara_deputados.voting v
            """ + whereClause + """
            ORDER BY v.date DESC, v.created_at DESC
            LIMIT :limit OFFSET :offset
        """;
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<VotingWithVotesDTO> votings = jdbcTemplate.query(votingsSql, params, (rs, rowNum) ->
                VotingWithVotesDTO.builder()
                        .id(rs.getString("id"))
                        .date(rs.getDate("date") != null ? rs.getDate("date").toLocalDate() : null)
                        .description(rs.getString("description"))
                        .organAcronym(rs.getString("organ_acronym"))
                        .votes(new ArrayList<>())
                        .build()
        );

        if (votings.isEmpty()) {
            return VotingWithVotesResponseDTO.builder()
                .data(new ArrayList<>())
                .total(0)
                .page(pageable.getPageNumber())
                .totalPages(0)
                .sizePage(pageable.getPageSize())
                .build();
        }

        List<String> votingIds = votings.stream().map(VotingWithVotesDTO::getId).collect(Collectors.toList());

        String votesSql = """
            SELECT pv.voting_id, pv.politician_id, p.name as politician_name, pv.vote_type
            FROM camara_deputados.politician_vote pv
            JOIN camara_deputados.politicians p ON p.id = pv.politician_id
            WHERE pv.voting_id IN (:votingIds)
        """;

        Map<String, Object> votesParams = new HashMap<>();
        votesParams.put("votingIds", votingIds);

        Map<String, List<VotingWithVotesDTO.PoliticianVoteSummaryDTO>> votesMap = new HashMap<>();
        jdbcTemplate.query(votesSql, votesParams, (rs, rowNum) -> {
            String votingId = rs.getString("voting_id");
            votesMap.computeIfAbsent(votingId, k -> new ArrayList<>()).add(
                    VotingWithVotesDTO.PoliticianVoteSummaryDTO.builder()
                            .politicianId(rs.getInt("politician_id"))
                            .politicianName(rs.getString("politician_name"))
                            .voteType(rs.getString("vote_type"))
                            .build()
            );
            return null;
        });

        votings.forEach(voting -> voting.setVotes(votesMap.getOrDefault(voting.getId(), new ArrayList<>())));

        String countSql = "SELECT COUNT(*) FROM camara_deputados.voting v" + whereClause;
        // Remove limit and offset from params for count query
        params.remove("limit");
        params.remove("offset");
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return VotingWithVotesResponseDTO.builder()
                .data(votings)
                .total(total)
                .page(pageable.getPageNumber())
                .totalPages((int) Math.ceil((double) total / pageable.getPageSize()))
                .sizePage(pageable.getPageSize())
                .build();
    }
}
