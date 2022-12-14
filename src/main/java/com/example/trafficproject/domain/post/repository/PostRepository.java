package com.example.trafficproject.domain.post.repository;

import com.example.trafficproject.domain.member.entity.Member;
import com.example.trafficproject.domain.post.dto.DailyPostCount;
import com.example.trafficproject.domain.post.dto.DailyPostCountRequest;
import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.util.PageHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

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
    private static final RowMapper<Post> POST_ROW_MAPPER = (ResultSet resultSet, int rowNum) -> Post.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .contents(resultSet.getString("contents"))
            .createDate(resultSet.getObject("createdDate", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .version(resultSet.getLong("version"))
            .likeCount(resultSet.getLong("likeCount"))
            .build();

    public Post save(Post post){
        if(post.getId() == null) return insert(post);
        return update(post);
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

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("size",pageable.getPageSize())
                .addValue("offset",pageable.getOffset());

        String sql = String.format("""
                select * from %s
                where memberId = :memberId
                order by %s
                limit :size
                offset :offset
                """,TABLE, PageHelper.orderBy(pageable.getSort()));

        List<Post> list = namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
        return new PageImpl(list, pageable, getCount(memberId));
    }

    private Long getCount(Long memberId){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId",memberId);
        String sql = String.format("""
                select count(*) from %s
                where memberId = :memberId
                """,TABLE);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    /***
     * batchupdate ????????? ?????? insert?????? ???????????? ???????????? ?????? ?????? ????????? insert (????????? ??????)
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

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size){ // JPA Naming ????????? ????????? ????????? ( ??? ??? JPA??? ???????????? )
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
        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndInMemberIdsAndOrderByIdDesc(Long id, List<Long> memberIds, int size){ // JPA Naming ????????? ????????? ????????? ( ??? ??? JPA??? ???????????? )
        if(memberIds.isEmpty()){
            return List.of();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds",memberIds)
                .addValue("id",id)
                .addValue("size",size);
        String sql = String.format("""
                select * from %s
                where memberId in (:memberIds) and id < :id
                order by id desc
                limit :size
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId",memberId)
                .addValue("size",size);
        String sql = String.format("""
                select * from %s
                where memberId = :memberId
                order by id desc
                limit :size
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(List<Long> memberIds, int size){
        if(memberIds.isEmpty()){
            return List.of();
        }

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds",memberIds)
                .addValue("size",size);
        String sql = String.format("""
                select * from %s
                where memberId in (:memberIds)
                order by id desc
                limit :size
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql, params, POST_ROW_MAPPER);
    }

    public List<Post> findByAllInId(List<Long> ids){
        if(ids.isEmpty()) {
            return List.of();
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids",ids);

        String sql = String.format("""
                select * from %s
                where memberId in (:ids)
                """,TABLE);
        return namedParameterJdbcTemplate.query(sql,params,POST_ROW_MAPPER);
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock){
        String sql = String.format("""
                Select * from %s where id = :postId
                """,TABLE);
        if(requiredLock){
            sql += "FOR UPDATE";
        }
        MapSqlParameterSource params = new MapSqlParameterSource().
                addValue("postId",postId);

        Post nullablePost = namedParameterJdbcTemplate.queryForObject(sql,params,POST_ROW_MAPPER);
        return Optional.ofNullable(nullablePost);
    }

    private Post update(Post post){
        // TODO: implemented soon
        String sql = String.format("UPDATE %s set memberId = :memberId, contents = :contents, likeCount = :likeCount, createdDate = :createdDate, createdAt = :createdAt, version = :version + 1 WHERE id = :id and version = :version", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        int updatedCount = namedParameterJdbcTemplate.update(sql,params);
        if(updatedCount == 0) {
            throw new RuntimeException("?????? ??????");
        }
        return post;
    }
}
