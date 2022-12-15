package com.example.trafficproject.domain.post.repository;

import com.example.trafficproject.domain.post.entity.PostLike;
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
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String TABLE = "PostLike";

    private static final RowMapper<Timeline> ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Timeline.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .postId(resultSet.getLong("postId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public PostLike save(PostLike postLike){
        if(postLike.getId() == null) return insert(postLike);
        throw new UnsupportedOperationException("timeline은 갱신을 지원하지 않습니다.");
    }

    private PostLike insert(PostLike postLike){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return PostLike.builder()
                .id(id)
                .memberId(postLike.getMemberId())
                .postId(postLike.getPostId())
                .createdAt(postLike.getCreatedAt())
                .build();
    }

    public Long count(Long postId){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId",postId);
        String sql = String.format("""
                select count(*) from %s
                where memberId = :postId
                """,TABLE);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }
}
