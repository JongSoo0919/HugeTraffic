package com.example.trafficproject.domain.member.dto;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String contents,
        LocalDateTime createdAt,
        Long likeCount) {
}
