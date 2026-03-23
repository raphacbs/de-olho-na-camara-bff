package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PropositionTramitationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class PropositionTramitationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<PropositionTramitationEntity> findLatestByPropositionId(Integer propositionId, int limit) {
        String sql = "SELECT * FROM proposition_tramitation WHERE proposition_id = :propId ORDER BY date_time DESC LIMIT :limit";
        Map<String, Object> params = new HashMap<>();
        params.put("propId", propositionId);
        params.put("limit", limit);

        return jdbcTemplate.query(sql, params, (rs, rowNum) -> mapRow(rs));
    }

    public List<PropositionTramitationEntity> findByPropositionIdWithDateFilter(Integer propositionId, LocalDateTime start, LocalDateTime end, int offset, int size) {
        StringBuilder sql = new StringBuilder("SELECT * FROM proposition_tramitation WHERE proposition_id = :propId");
        Map<String, Object> params = new HashMap<>();
        params.put("propId", propositionId);

        if (start != null) {
            sql.append(" AND date_time >= :start");
            params.put("start", start);
        }
        if (end != null) {
            sql.append(" AND date_time <= :end");
            params.put("end", end);
        }

        sql.append(" ORDER BY date_time DESC LIMIT :size OFFSET :offset");
        params.put("size", size);
        params.put("offset", offset);

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> mapRow(rs));
    }

    public Integer countByPropositionIdWithDateFilter(Integer propositionId, LocalDateTime start, LocalDateTime end) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(1) FROM proposition_tramitation WHERE proposition_id = :propId");
        Map<String, Object> params = new HashMap<>();
        params.put("propId", propositionId);
        if (start != null) {
            sql.append(" AND date_time >= :start");
            params.put("start", start);
        }
        if (end != null) {
            sql.append(" AND date_time <= :end");
            params.put("end", end);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
    }

    public void upsertTramitation(List<PropositionTramitationEntity> list) {
        if (list == null || list.isEmpty()) return;

        String sql = "INSERT INTO proposition_tramitation (proposition_id, date_time, sequence, org_acronym, org_uri, last_reporter_uri, regime, tramitation_description, tramitation_type_code, situation_description, situation_code, dispatch, url, scope, appreciation, created_at, updated_at) " +
                "VALUES (:propositionId, :dateTime, :sequence, :orgAcronym, :orgUri, :lastReporterUri, :regime, :tramitationDescription, :tramitationTypeCode, :situationDescription, :situationCode, :dispatch, :url, :scope, :appreciation, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP) " +
                "ON CONFLICT (proposition_id, sequence) DO UPDATE SET " +
                "date_time = EXCLUDED.date_time, org_acronym = EXCLUDED.org_acronym, org_uri = EXCLUDED.org_uri, last_reporter_uri = EXCLUDED.last_reporter_uri, regime = EXCLUDED.regime, tramitation_description = EXCLUDED.tramitation_description, tramitation_type_code = EXCLUDED.tramitation_type_code, situation_description = EXCLUDED.situation_description, situation_code = EXCLUDED.situation_code, dispatch = EXCLUDED.dispatch, url = EXCLUDED.url, scope = EXCLUDED.scope, appreciation = EXCLUDED.appreciation, updated_at = CURRENT_TIMESTAMP";

        List<Map<String, Object>> paramsList = list.stream().map(e -> {
            Map<String, Object> p = new HashMap<>();
            p.put("propositionId", e.getPropositionId());
            p.put("dateTime", e.getDateTime());
            p.put("sequence", e.getSequence());
            p.put("orgAcronym", e.getOrgAcronym());
            p.put("orgUri", e.getOrgUri());
            p.put("lastReporterUri", e.getLastReporterUri());
            p.put("regime", e.getRegime());
            p.put("tramitationDescription", e.getTramitationDescription());
            p.put("tramitationTypeCode", e.getTramitationTypeCode());
            p.put("situationDescription", e.getSituationDescription());
            p.put("situationCode", e.getSituationCode());
            p.put("dispatch", e.getDispatch());
            p.put("url", e.getUrl());
            p.put("scope", e.getScope());
            p.put("appreciation", e.getAppreciation());
            return p;
        }).collect(Collectors.toList());

        @SuppressWarnings("unchecked")
        Map<String, Object>[] batchParams = paramsList.toArray(new Map[0]);

        jdbcTemplate.batchUpdate(sql, batchParams);
    }

    private PropositionTramitationEntity mapRow(ResultSet rs) throws SQLException {
        return PropositionTramitationEntity.builder()
                .id(rs.getInt("id"))
                .propositionId(rs.getInt("proposition_id"))
                .dateTime(rs.getTimestamp("date_time") != null ? rs.getTimestamp("date_time").toLocalDateTime() : null)
                .sequence(rs.getObject("sequence") != null ? rs.getInt("sequence") : null)
                .orgAcronym(rs.getString("org_acronym"))
                .orgUri(rs.getString("org_uri"))
                .lastReporterUri(rs.getString("last_reporter_uri"))
                .regime(rs.getString("regime"))
                .tramitationDescription(rs.getString("tramitation_description"))
                .tramitationTypeCode(rs.getString("tramitation_type_code"))
                .situationDescription(rs.getString("situation_description"))
                .situationCode(rs.getString("situation_code"))
                .dispatch(rs.getString("dispatch"))
                .url(rs.getString("url"))
                .scope(rs.getString("scope"))
                .appreciation(rs.getString("appreciation"))
                .createdAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }
}
