package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.SpeechEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SpeechRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<SpeechEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {

        String sql = """
            SELECT 
                politician_id,
                start_datetime,
                end_datetime,
                title_event,
                start_datetime_event,
                end_datetime_event,
                keywords,
                summary,
                speech_type,
                transcription,
                event_uri,
                audio_url,
                text_url,
                video_url,
                created_at,
                updated_at
            FROM speeches
            WHERE politician_id = :politicianId
            ORDER BY start_datetime DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = """
            SELECT COUNT(*) 
            FROM speeches
            WHERE politician_id = :politicianId
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<SpeechEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }


    public void upsertSpeech(SpeechEntity s) {

        String sql = """
            INSERT INTO speeches (
                politician_id,
                start_datetime,
                end_datetime,
                title_event,
                start_datetime_event,
                end_datetime_event,
                keywords,
                summary,
                speech_type,
                transcription,
                event_uri,
                audio_url,
                text_url,
                video_url,
                created_at,
                updated_at
            ) VALUES (
                :politicianId,
                :startDateTime,
                :endDateTime,
                :titleEvent,
                :startDateTimeEvent,
                :endDateTimeEvent,
                :keywords,
                :summary,
                :speechType,
                :transcription,
                :eventUri,
                :audioUrl,
                :textUrl,
                :videoUrl,
                CURRENT_TIMESTAMP,
                CURRENT_TIMESTAMP
            )
            ON CONFLICT (politician_id, start_datetime) DO UPDATE SET
                end_datetime = EXCLUDED.end_datetime,
                start_datetime_event = EXCLUDED.start_datetime_event,
                end_datetime_event = EXCLUDED.end_datetime_event,
                keywords = EXCLUDED.keywords,
                summary = EXCLUDED.summary,
                speech_type = EXCLUDED.speech_type,
                transcription = EXCLUDED.transcription,
                event_uri = EXCLUDED.event_uri,
                audio_url = EXCLUDED.audio_url,
                text_url = EXCLUDED.text_url,
                video_url = EXCLUDED.video_url,
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", s.getPoliticianId());
        params.put("startDateTime", s.getStartDateTime());
        params.put("endDateTime", s.getEndDateTime());
        params.put("titleEvent", s.getTitleEvent());
        params.put("startDateTimeEvent", s.getStartDateTimeEvent());
        params.put("endDateTimeEvent", s.getEndDateTimeEvent());
        params.put("keywords", s.getKeywords());
        params.put("summary", s.getSummary());
        params.put("speechType", s.getSpeechType());
        params.put("transcription", s.getTranscription());
        params.put("eventUri", s.getEventUri());
        params.put("audioUrl", s.getAudioUrl());
        params.put("textUrl", s.getTextUrl());
        params.put("videoUrl", s.getVideoUrl());

        jdbcTemplate.update(sql, params);
    }


    private SpeechEntity mapRow(ResultSet rs) throws SQLException {
        return SpeechEntity.builder()
                .politicianId(rs.getInt("politician_id"))
                .startDateTime(rs.getTimestamp("start_datetime") != null ?
                        rs.getTimestamp("start_datetime").toLocalDateTime() : null)
                .endDateTime(rs.getTimestamp("end_datetime") != null ?
                        rs.getTimestamp("end_datetime").toLocalDateTime() : null)
                .titleEvent(rs.getString("title_event"))
                .startDateTimeEvent(rs.getTimestamp("start_datetime_event") != null ?
                        rs.getTimestamp("start_datetime_event").toLocalDateTime() : null)
                .endDateTimeEvent(rs.getTimestamp("end_datetime_event") != null ?
                        rs.getTimestamp("end_datetime_event").toLocalDateTime() : null)
                .keywords(rs.getString("keywords"))
                .summary(rs.getString("summary"))
                .speechType(rs.getString("speech_type"))
                .transcription(rs.getString("transcription"))
                .eventUri(rs.getString("event_uri"))
                .audioUrl(rs.getString("audio_url"))
                .textUrl(rs.getString("text_url"))
                .videoUrl(rs.getString("video_url"))
                .createdAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ?
                        rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .build();
    }

}
