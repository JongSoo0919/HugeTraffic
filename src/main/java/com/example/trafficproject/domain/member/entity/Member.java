package com.example.fastcampusmysql.domain.member.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Member {
    private final Long id;
    private String nickname; // 닉네임이 변경 가능하기에 final로 선언하지 않음
    private final String email;
    private final LocalDate birthday;
    private final LocalDateTime createdAt;

    private final static Long NAME_MAX_LENGTH = 10L;
    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthday, LocalDateTime createdAt){
        //JPA로 리팩토링 가능토록
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.birthday = Objects.requireNonNull(birthday);

        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);

        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    /***
     * 변경이니 public으로 열어둠
     * @param nickname
     */
    public void changeNickname(String nickname){
        Objects.requireNonNull(nickname);
        validateNickname(nickname);
        this.nickname = nickname;
    }

    private void validateNickname(String nickname){
        // 커스텀 익셉션 추가 하면 좋음
        Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH,"최대 길이를 초과했습니다.");
    }

}
