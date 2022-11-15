package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MemberRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final static String TABLE = "member";
    private final RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member
            .builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthday(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();
    /**
     * Member의 id를 보고 갱신 또는 삽입, 반환 값은 id를 담아서 반환
     * @param member
     * @return
     */
    public Member save(Member member){
        if(member.getId() == null){
            return insert(member);
        }
        return update(member);
    }

    /**
     * id(pk)를 통해 member를 조회
     * @return
     */
    public Optional<Member> findById(long id){
        /*
            select * from Member where id = id;
         */
        String sql = String.format("SELECT * FROM %s WHERE id = :id",TABLE);
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id",id);
        return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql,param,rowMapper));
    }

    private Member insert(Member member){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        return Member
                .builder()
                .id(id.longValue())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .build();
    }
    private Member update(Member member){
        // TODO: implemented soon
        String sql = String.format("UPDATE %s set email = :email, nickname = :nickname, birthday = :birthday WHERE id = :id", TABLE);
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql,params);
        return member;
    }

    // sql부분 중복을 제거할 수 있는 방법도 찾아보자
    public List<Member> findAllByIdIn(List<Long> ids){
        if(ids.isEmpty()) return List.of();

        String sql = String.format("SELECT * FROM %s WHERE id in (:ids)",TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource("ids",ids);
        return namedParameterJdbcTemplate.query(sql,params,rowMapper);
    }
}
