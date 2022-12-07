package com.example.trafficproject.domain.post.service;

import com.example.trafficproject.domain.post.entity.Post;
import com.example.trafficproject.domain.post.entity.Timeline;
import com.example.trafficproject.domain.post.repository.TimelineRepository;
import com.example.trafficproject.util.CursorRequest;
import com.example.trafficproject.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineReadService {
    private final TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest){
        List<Timeline> timelines = findAllById(memberId, cursorRequest);
        Long nextKey =  timelines.stream()
                .mapToLong(Timeline::getId)
                .min().orElse(CursorRequest.EMPTY_KEY);

        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllById(Long memberId, CursorRequest cursorRequest){
        if(cursorRequest.hasKey()){
            return timelineRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }
        return timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }
}
