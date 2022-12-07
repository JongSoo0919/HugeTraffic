package com.example.trafficproject.domain.post.repository;

import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.entity.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class TimelineRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String TABLE = "Timeline";

    private static final RowMapper<Timeline> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Timeline.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .postId(resultSet.getLong("postId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public List<Timeline> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("size",size);
        String sql = String.format("""
                select * from %s
                where memberId = :memberId
                order by id desc
                limit :size
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Timeline> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size){ // JPA Naming 기법을 그대로 따라감 ( 추 후 JPA로 리팩토링 )
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("id",id)
                .addValue("size",size);
        String sql = String.format("""
                select * from %s
                where memberId = :memberId and id < :id
                order by id desc
                limit :size
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public Timeline save(Timeline timeline){
        if(timeline.getId() == null) return insert(timeline);
        throw new UnsupportedOperationException("timeline은 갱신을 지원하지 않습니다.");
    }

    private Timeline insert(Timeline timeline){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(timeline);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Timeline.builder()
                .id(id)
                .memberId(timeline.getMemberId())
                .postId(timeline.getPostId())
                .createdAt(timeline.getCreatedAt())
                .build();
    }

    public void bulkInsert(List<Timeline> timelines){
        String sql = String.format("insert into %s (memberId, postId, createdAt)" +
                "values(:memberId, :postId, :createdAt)",TABLE);
        SqlParameterSource [] params = timelines
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql,params);
    }
}
