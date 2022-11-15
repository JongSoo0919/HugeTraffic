package com.example.fastcampusmysql.domain.member.dto;

import java.time.LocalDate;

/**
 * dto, getter , setter를 자동으로 생성해주는 record(java 16이상)
 */
public record MemberDto(
        long id,
        String email,
        String nickname,
        LocalDate birthday
) {
}
