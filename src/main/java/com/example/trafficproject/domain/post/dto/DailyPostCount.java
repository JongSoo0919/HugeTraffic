package com.example.trafficproject.domain.post.dto;

import java.time.LocalDate;

/***
 * 캘린더 조회 시 반환할 response
 */
public record DailyPostCount(
        Long memberId,
        LocalDate date,
        Long postCount
) {
}
