package com.example.trafficproject.domain.post.service;

import com.example.trafficproject.domain.post.entity.Timeline;
import com.example.trafficproject.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {
    private final TimelineRepository timelineRepository;

    /***
     * 게시물 작성시 내 타임라인을 볼 수 있는 팔로워들에게 알림
     * @param postId
     * @param toMemberIds
     */
    public void deliveryToTimeline(Long postId, List<Long> toMemberIds){
        List<Timeline> timelines = toMemberIds.stream()
                .map(memberId -> toTimeline(postId, memberId))
                .toList();

        timelineRepository.bulkInsert(timelines);
    }

    private Timeline toTimeline(Long postId, Long memberId) {
        return Timeline.builder()
                .memberId(memberId)
                .postId(postId)
                .build();
    }
}
