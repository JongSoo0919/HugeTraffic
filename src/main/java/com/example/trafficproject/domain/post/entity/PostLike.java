package com.example.trafficproject.domain.post.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class PostLike {
    private final Long id;
    private final Long memberId;
    private final Long postId;
    private final LocalDateTime createdAt;

    @Builder
    public PostLike(Long id, Long memberId, Long postId, LocalDateTime createdAt) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.postId = Objects.requireNonNull(postId);
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt; // 생성 시점에만 존재해야하므로 id값이 null일 때만 하는게 맞음.
    }
}
