package com.example.trafficproject.domain.post.dto;

/***
 * 게시물 작성 시 받을 parameter
 */
public record PostCommand(
        Long memberId,
        String contents
) {
}
