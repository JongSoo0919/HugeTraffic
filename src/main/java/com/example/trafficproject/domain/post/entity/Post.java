package com.example.trafficproject.domain.post.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Post {
    private final Long id;          // seq
    private final Long memberId;    // 작성자
    private final String contents;  // 내용
    private final LocalDate createdDate;
    private Long likeCount;
    private final LocalDateTime createdAt;
    private Long version;
    //이미지 업로드
    //비디오 업로드
    //게시물 수정 등등,, develop


    @Builder
    public Post(Long id, Long memberId, String contents, Long likeCount, LocalDate createDate, LocalDateTime createdAt, Long version) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.likeCount = likeCount == null ? 0L : likeCount;
        this.createdDate = createDate == null ? LocalDate.now() : createDate;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.version = version == null ? 0L : version;
    }

    public void increamentLikeCount(){
        likeCount += 1;
    }
}
