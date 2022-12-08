package com.example.trafficproject.application.usecase;

import com.example.trafficproject.domain.follow.entity.Follow;
import com.example.trafficproject.domain.follow.service.FollowReadService;
import com.example.trafficproject.domain.post.dto.PostCommand;
import com.example.trafficproject.domain.post.service.PostWriteService;
import com.example.trafficproject.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
    private final PostWriteService postWriteService;
    private final FollowReadService followReadService;
    private final TimelineWriteService timelineWriteService;

//    @Transactional
//    게시물 하나를 작성할 때, 팔로워가 많다면 트랜잭션의 작업이 길어지게 됨.
//    undo Log 를 보관해야 하는 시간이 그만큼 길어지고,
//    lock을 잡게 될 시 lock을 점유하고 있는 시간이 길어지므로 성능에 영향이 감.
//    트랜잭션 사용시 트랜잭션 범위를 짧게 하는 것이 좋음.
//    트랜잭션이 길어지면 DB의 커넥션을 점유하고 있기 때문에, 많은 사용자가 오랫동안 트랜잭션이 실행 될 시 커넥션 풀 고갈로 이어질 수 있음.
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
