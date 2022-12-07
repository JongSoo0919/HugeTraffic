package com.example.trafficproject.application.usecase;

import com.example.trafficproject.domain.follow.entity.Follow;
import com.example.trafficproject.domain.follow.service.FollowReadService;
import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.service.PostWriteService;
import com.example.trafficproject.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand){
        Long postId = postWriteService.create(postCommand);
        List<Long> followerMemberIds = followReadService
                .getFollowers(postCommand.memberId())
                .stream()
                .map(Follow::getToMemberId)
                .toList();
        timelineWriteService.deliveryToTimeline(postId, followerMemberIds);
        return postId;
    }
}
