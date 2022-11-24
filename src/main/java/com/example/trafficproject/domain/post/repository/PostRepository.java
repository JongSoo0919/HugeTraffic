package com.example.trafficproject.domain.post.repository;

import com.example.trafficproject.domain.post.dto.DailyPostCount;
import com.example.trafficproject.domain.post.dto.DailyPostCountRequest;
import com.example.trafficproject.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepository {
    private static final String TABLE = "Post";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final RowMapper<DailyPostCount> DAILY_POST_COUNT_MAPPER = (ResultSet resultSet, int rowNum) -> new DailyPostCount(
            resultSet.getLong("memberId"),
            resultSet.getObject("createdDate",LocalDate.class),
            resultSet.getLong("count")
    );
    public Post save(Post post){
        if(post.getId() == null) return insert(post);
        throw new UnsupportedOperationException("Post는 갱신을 지원하지 않습니다.");
    }

    public List<DailyPostCount> groupByCreateDate(DailyPostCountRequest request){
        String sql = String.format("""
                   select createdDate, memberId, count(id) as count 
                   from %s
                   where memberId = :memberId and createdDate between :firstDate and :lastDate
                   group by createdDate, memberId
                """, TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params,DAILY_POST_COUNT_MAPPER);

    }

    /***
     * batchupdate 함수를 통해 insert문을 단건으로 집어넣는 것이 아닌 한번에 insert (부하를 줄임)
     * @param posts
     */
    public void bulkInsert(List<Post> posts){
        String sql = String.format("insert into %s (memberId, contents, createdDate, createdAt)" +
                "values(:memberId, :contents, :createdDate, :createdAt)",TABLE);
        SqlParameterSource [] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql,params);
    }

    private Post insert(Post post){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdAt(post.getCreatedAt())
                .createDate(post.getCreatedDate())
                .build();
    }




}
